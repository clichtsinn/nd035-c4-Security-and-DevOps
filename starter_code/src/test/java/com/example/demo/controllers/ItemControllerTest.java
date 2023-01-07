package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    @Autowired
    private ItemController itemController;

    @Autowired
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void findItemById() throws Exception {

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Item> response = itemController.getItemById(item.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item returnItem = response.getBody();
        assertNotNull(returnItem);
        assertEquals(1L, (long) returnItem.getId());
        assertEquals("Round Widget", returnItem.getName());
        assertEquals(BigDecimal.valueOf(2.99), returnItem.getPrice());
        assertEquals("A widget that is round", returnItem.getDescription());
    }

    @Test
    public void notFindItemById() throws Exception {

        Long test = 1L;

        final ResponseEntity<Item> response = itemController.getItemById(test);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findItemByName() throws Exception {

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");
        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findByName(item.getName())).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemz = response.getBody();
        assertNotNull(itemz);
        assertEquals(1L, (long) itemz.get(0).getId());
        assertEquals("Round Widget", itemz.get(0).getName());
        assertEquals(BigDecimal.valueOf(2.99), itemz.get(0).getPrice());
        assertEquals("A widget that is round", itemz.get(0).getDescription());
    }
}
