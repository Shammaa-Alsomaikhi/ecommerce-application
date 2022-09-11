package com.example.demo.controllersTest;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        User user = new User();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        List<Item> items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(2.99);
        cart.setTotal(total);

        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
    }

    @Test
    public void submitOrderHappyPath() {
        final ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final UserOrder actualOrder = response.getBody();
        assertNotNull(actualOrder);
        assertEquals(1, actualOrder.getItems().size());
    }

    @Test
    public void submitOrderWithNonExistingUser() {
        final ResponseEntity<UserOrder> response = orderController.submit("shammaa");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserHappyPath() {
        final ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("test");
        assertNotNull(ordersForUser);
        assertEquals(200, ordersForUser.getStatusCodeValue());

        final List<UserOrder> actualOrders = ordersForUser.getBody();
        assertNotNull(actualOrders);
        assertEquals(0, actualOrders.size());
    }

    @Test
    public void getOrdersForNonExistingUser() {
        final ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("shammaa");
        assertNotNull(ordersForUser);
        assertEquals(404, ordersForUser.getStatusCodeValue());

    }
}
