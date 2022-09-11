package com.example.demo.controllersTest;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    public void addToCartHappyPath() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("test");

        final ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final Cart actualCart = response.getBody();
        assertNotNull(actualCart);
        assertEquals(BigDecimal.valueOf(2.99), actualCart.getTotal());
    }

    @Test
    public void addToCartWithNonExistingUser() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("shammaa");

        final ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartWithNonExistingItem() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("test");

        final ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartHappyPath() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("test");

        final ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final Cart actualCart = response.getBody();
        assertNotNull(actualCart);
        List<Item> items = actualCart.getItems();
        assertNotNull(items);
        assertEquals(0, items.size());
        assertEquals(new BigDecimal(0.00), actualCart.getTotal());
    }


    @Test
    public void removeFromCartWithNonExistingUser() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("shammaa");

        final ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartWithNonExistingItem() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("test");

        final ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
