package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    @Autowired
    private CartController cartController;

    @Autowired
    private CartRepository cartRepository = mock(CartRepository.class);

    @Autowired
    private UserRepository userRepository = mock(UserRepository.class);

    @Autowired
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addRemoveCart() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        user.setCart(cart);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(item.getId());
        request.setQuantity(1);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cartAdd = response.getBody();
        assertEquals(new BigDecimal("2.99"), cartAdd.getTotal());
        assertEquals("Round Widget", cartAdd.getItems().get(0).getName());
        assertEquals("test", cartAdd.getUser().getUsername());
        assertEquals(1L, (long) cartAdd.getId());

        final ResponseEntity<Cart> responseRemove = cartController.removeFromcart(request);

        assertNotNull(responseRemove);
        assertEquals(200, response.getStatusCodeValue());

        Cart cartRemove = responseRemove.getBody();
        assertEquals(new BigDecimal("0.00"), cartRemove.getTotal());
        assertEquals("test", cartRemove.getUser().getUsername());
        assertEquals(1L, (long) cartRemove.getId());
    }
}
