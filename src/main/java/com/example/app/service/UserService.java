package com.example.app.service;

import com.example.app.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    User getUser(String username);

    void addFriend(String username, String friendName);

    List<User> getFriends(String username);
}
