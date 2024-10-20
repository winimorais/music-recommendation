package com.example.app.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @NotBlank(message = "User name cannot be empty or null")
    private String name;

    private Set<Song> playlist = new HashSet<>();

    private Set<User> friends = new HashSet<>();

    public User(String name) {
        this.name = name;
    }
}
