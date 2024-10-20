package com.example.controller;


import com.example.app.controller.UserController;
import com.example.app.dto.FriendRequest;
import com.example.app.model.User;
import com.example.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User("Alice");
        user2 = new User("Bob");
    }

    @Test
    void testAddUser() {
        // Como o método é void, não precisamos definir um retorno
        doNothing().when(userService).addUser(any(User.class)); // Adicionando a expectativa

        ResponseEntity<String> response = userController.addUser(user1);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("User added.", response.getBody());
        verify(userService, times(1)).addUser(user1); // Verifica se o método foi chamado uma vez
    }

    @Test
    void testGetUser() {
        when(userService.getUser("Alice")).thenReturn(user1);

        ResponseEntity<User> response = userController.getUser("Alice");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user1, response.getBody());
        verify(userService, times(1)).getUser("Alice");
    }

    @Test
    void testAddFriend() {
        FriendRequest friendRequest = new FriendRequest("Bob");

        ResponseEntity<String> response = userController.addFriend("Alice", friendRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Friend added successfully", response.getBody());
        verify(userService, times(1)).addFriend("Alice", "Bob");
    }

    @Test
    void testGetFriends() {
        when(userService.getFriends("Alice")).thenReturn(Collections.singletonList(user2));

        ResponseEntity<List<User>> response = userController.getFriends("Alice");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(user2, response.getBody().get(0));
        verify(userService, times(1)).getFriends("Alice");
    }
}
