package com.example.app.controller;

import com.example.app.dto.FriendRequest;
import com.example.app.model.User;
import com.example.app.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Add a new user
    @PostMapping
    public ResponseEntity<String> addUser(@Valid @RequestBody User user) {
        log.info("Adding user: {}", user.getName());
        userService.addUser(user);
        return ResponseEntity.status(201).body("User added.");
    }

    // Get a user by username
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        log.info("Fetching user '{}'", username);
        User user = userService.getUser(username);
        return ResponseEntity.ok(user);
    }

    // Add a friend to a user
    @PostMapping("/{username}/friends")
    public ResponseEntity<String> addFriend(
            @PathVariable String username, @Valid @RequestBody FriendRequest friendRequest) {
        log.info("Adding friend '{}' to user '{}'", friendRequest.getName(), username);
        userService.addFriend(username, friendRequest.getName());
        return ResponseEntity.ok("Friend added successfully");
    }

    // Get a user's friends list
    @GetMapping("/{username}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable String username) {
        log.info("Fetching friends for user '{}'", username);
        List<User> friends = userService.getFriends(username);
        return ResponseEntity.ok(friends);
    }
}