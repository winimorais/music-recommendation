package com.example.app;

import com.example.app.model.Song;
import com.example.app.model.User;
import com.example.app.service.MusicServiceImpl;
import com.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class MusicApp implements CommandLineRunner {

    @Autowired
    private MusicServiceImpl musicService;

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(MusicApp.class, args);
    }

    @Override
    public void run(String... args) {
        initializeMusicLibrary();
    }

    private void initializeMusicLibrary() {
        // Create a user
        User user = createUser("Zoe");

        // Create a new user
        User newUser = createUser("Wini");

        // Create and add songs
        addSongs();

        // Add a song to the user's playlist
        addSongToUserPlaylist(user.getName(), "In the End");

        // Existing user adds the new user as a friend
        addFriend(user.getName(), newUser.getName());

        // Get and display recommendations for the user
        displayRecommendations(user.getName());

        // Display friends of the existing user
        displayFriends(user.getName());

        // Add more songs and check recommendations again
        addMoreSongs();
        displayRecommendations(user.getName());
    }

    private User createUser(String username) {
        User user = new User(username);
        userService.addUser(user);
        return user;
    }

    private void addSongs() {
        Song song1 = new Song("In the End", "Rock", "Linkin Park", 105, 2000, 95);
        Song song2 = new Song("Uptown Funk", "Pop", "Bruno Mars", 105, 2014, 95);
        Song song3 = new Song("Numb", "Rock", "Linkin Park", 105, 2000, 95);
        Song song4 = new Song("24K Magic", "Pop", "Bruno Mars", 105, 2016, 90);
        Song song5 = new Song("Beautiful", "Hip Hop", "Snoop Dogg", 90, 2006, 85);

        musicService.addSong(song1);
        musicService.addSong(song2);
        musicService.addSong(song3);
        musicService.addSong(song4);
        musicService.addSong(song5);
    }

    private void addSongToUserPlaylist(String username, String songName) {
        musicService.addSongToPlaylist(username, songName);
    }

    private void addFriend(String username, String friendName) {
        userService.addFriend(username, friendName);
    }

    private void displayFriends(String username) {
        List<User> friends = userService.getFriends(username);
        System.out.println("Friends of " + username + ":");
        friends.forEach(friend -> System.out.println(friend.getName()));
    }

    private void displayRecommendations(String username) {
        List<Song> recommendations = musicService.getRecommendations(username);
        System.out.println("Recommendations for " + username + ":");
        recommendations.forEach(song -> System.out.println(song.getName()));
    }

    private void addMoreSongs() {
        Song song6 = new Song("Moon", "Pop", "Bruno Mars", 105, 2016, 90);
        Song song7 = new Song("Black Vanilla", "Hip Hop", "Snoop Dogg", 99, 2003, 82);

        musicService.addSong(song6);
        musicService.addSong(song7);
    }
}