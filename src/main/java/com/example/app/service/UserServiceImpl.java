package com.example.app.service;

import com.example.app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        validateUserInput(user);
        checkIfUserExists(user.getName());
        users.put(user.getName(), user);
        log.info("User '{}' added.", user.getName());
    }

    @Override
    public User getUser(String username) {
        return validateUser(username);
    }

    @Override
    public void addFriend(String username, String friendName) {
        User user = validateUser(username);
        User friend = validateUser(friendName);

        checkIfSelfFriend(user, friend);
        checkIfFriendAlreadyExists(user, friend);

        addFriendToUser(user, friend);
    }

    @Override
    public List<User> getFriends(String username) {
        User user = validateUser(username);
        return new ArrayList<>(user.getFriends());
    }

    private void validateUserInput(User user) {
        if (user == null || user.getName() == null || user.getName().isEmpty()) {
            log.error("Invalid user data: user is null or has an empty name.");
            throw new IllegalArgumentException("User name cannot be null or empty.");
        }
    }

    private void checkIfUserExists(String username) {
        if (users.containsKey(username)) {
            log.warn("User '{}' already exists.", username);
            throw new IllegalArgumentException("User '" + username + "' already exists.");
        }
    }

    private User validateUser(String username) {
        log.info("Validating user '{}'", username);
        log.info("Current users: {}", users.keySet());

        User user = users.get(username);
        if (user == null) {
            log.error("User '{}' not found.", username);
            throw new IllegalArgumentException("User '" + username + "' not found.");
        }
        return user;
    }

    private void checkIfSelfFriend(User user, User friend) {
        if (user.equals(friend)) {
            log.warn("User '{}' cannot add themselves as a friend.", user.getName());
            throw new IllegalArgumentException("User cannot add themselves as a friend.");
        }
    }

    private void addFriendToUser(User user, User friend) {
        if (!user.getFriends().contains(friend)) {
            user.getFriends().add(friend);
            log.info("User '{}' added '{}' as a friend.", user.getName(), friend.getName());
        } else {
            log.warn("User '{}' already has '{}' as a friend.", user.getName(), friend.getName());
        }
    }

    private void checkIfFriendAlreadyExists(User user, User friend) {
        if (user.getFriends().contains(friend)) {
            log.warn("User '{}' already has '{}' as a friend.", user.getName(), friend.getName());
            throw new IllegalArgumentException("User '" + user.getName() + "' already has '" + friend.getName() + "' as a friend.");
        }
    }
}