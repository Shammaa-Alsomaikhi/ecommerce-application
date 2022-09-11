package com.example.demo.controllersTest;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath() {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        final User actualUser = response.getBody();
        assertNotNull(actualUser);
        assertEquals(0, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("thisIsHashed", actualUser.getPassword());
    }

    @Test
    public void createUserWithPasswordLengthLessThan7() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("abc");
        createUserRequest.setConfirmPassword("abc");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUserWithMismatchPassword() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("abcdefg");
        r.setConfirmPassword("abcabca");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUserByIdHappyPath() {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        User user = response.getBody();
        when(userRepository.findById(0L)).thenReturn(Optional.of(user));

        final ResponseEntity<User> userResponseEntity = userController.findById(0L);
        User actualUser = userResponseEntity.getBody();
        assertNotNull(actualUser);
        assertEquals(0, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("thisIsHashed", actualUser.getPassword());
    }


    @Test
    public void findUserByIdWithNonExistingUser() {
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findUserByUserNameHappyPath() {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        User user = response.getBody();
        when(userRepository.findByUsername("test")).thenReturn(user);

        final ResponseEntity<User> userResponseEntity = userController.findByUserName("test");
        User actualUser = userResponseEntity.getBody();
        assertNotNull(actualUser);
        assertEquals(0, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("thisIsHashed", actualUser.getPassword());
    }

    @Test
    public void findUserByUserNameWithNonExistingUser() {
        final ResponseEntity<User> response = userController.findByUserName("shammaa");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
