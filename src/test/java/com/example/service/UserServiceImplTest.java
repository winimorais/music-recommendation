package com.example.service;

import com.example.app.model.User;
import com.example.app.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {

    private final User user1 = new User("Alice");
    private final User user2 = new User("Bob");
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl();
        userService.addUser(user1); // Add user1 to the service
    }

    @Test
    public void testAddUser_Success() {
        User user3 = new User("Charlie");
        userService.addUser(user3);

        assertEquals(user3, userService.getUser("Charlie"));
    }

    @Test
    public void testAddUser_UserAlreadyExists() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user1); // Trying to add an existing user
        });
        assertEquals("User 'Alice' already exists.", exception.getMessage());
    }

    @Test
    public void testGetUser_Success() {
        User fetchedUser = userService.getUser("Alice");
        assertEquals(user1, fetchedUser);
    }

    @Test
    public void testGetUser_UserNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUser("Charlie"); // User not found
        });
        assertEquals("User 'Charlie' not found.", exception.getMessage());
    }

    @Test
    public void testAddFriend_Success() {
        userService.addUser(user2); // Add user2 to the service
        userService.addFriend("Alice", "Bob");

        List<User> friends = userService.getFriends("Alice");
        assertTrue(friends.contains(user2));
    }

    @Test
    public void testAddFriend_SelfFriendNotAllowed() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addFriend("Alice", "Alice"); // Self-friend not allowed
        });
        assertEquals("User cannot add themselves as a friend.", exception.getMessage());
    }

    @Test
    public void testAddFriend_FriendAlreadyExists() {
        userService.addUser(user2); // Add user2 to the service
        userService.addFriend("Alice", "Bob"); // First add
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addFriend("Alice", "Bob"); // Adding the same friend again
        });
        assertEquals("User 'Alice' already has 'Bob' as a friend.", exception.getMessage());
    }

    @Test
    public void testGetFriends_NoFriends() {
        List<User> friends = userService.getFriends("Alice");
        assertTrue(friends.isEmpty());
    }

    @Test
    public void testGetFriends_Success() {
        userService.addUser(user2); // Add user2 to the service
        userService.addFriend("Alice", "Bob");

        List<User> friends = userService.getFriends("Alice");
        assertEquals(1, friends.size());
        assertTrue(friends.contains(user2));
    }
}
