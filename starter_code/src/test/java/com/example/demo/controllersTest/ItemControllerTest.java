package com.example.demo.controllersTest;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByName("Round Widget")).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void getItemsHappyPath() {
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        final  List<Item> actualItems = response.getBody();
        assertNotNull(actualItems);
        assertEquals(1, actualItems.size());
    }

    @Test
    public void getItemByIdHappyPath() {
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final List<Item> actualItems = response.getBody();
        assertNotNull(actualItems);
        assertEquals(1, actualItems.size());
    }

    @Test
    public void getItemByIdWithNonExistingId() {
        final ResponseEntity<Item> response = itemController.getItemById(2L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameHappyPath() {
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final List<Item> actualItems = response.getBody();
        assertNotNull(actualItems);
        assertEquals(1, actualItems.size());
    }

    @Test
    public void getItemsByNameWithNonExistingName() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
