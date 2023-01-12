package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private UserRepository userRepository = mock(UserRepository.class);

    @Autowired
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void getOrderHistory() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(new BigDecimal("2.99"));
        item.setDescription("It's a Round Widget");
        List<Item> items = new ArrayList<>();
        items.add(item);

        UserOrder order = new UserOrder();
        order.setId(1L);
        order.setUser(user);
        order.setItems(items);
        order.setTotal(new BigDecimal("2.99"));
        List<UserOrder> orders = new ArrayList<>();
        orders.add(order);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orders);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> ordersRe = response.getBody();
        assertNotNull(ordersRe);
        assertEquals(1L, (long) ordersRe.get(0).getId());
        assertEquals(new BigDecimal("2.99"), ordersRe.get(0).getTotal());
        assertEquals("Round Widget", ordersRe.get(0).getItems().get(0).getName());
    }
}
