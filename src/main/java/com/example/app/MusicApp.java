package com.example.app;

import com.example.app.model.Song;
import com.example.app.model.User;
import com.example.app.service.MusicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class MusicApp implements CommandLineRunner {

    @Autowired
    private final MusicServiceImpl musicService;

    @Autowired
    public MusicApp(MusicServiceImpl musicService) {
        this.musicService = musicService;
    }

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

        // Create and add songs
        addSongs();

        // Add a song to the user's playlist
        addSongToUserPlaylist(user.getName(), "In the End");

        // Get and display recommendations for the user
        displayRecommendations(user.getName());

        // Add more songs and check recommendations again
        addMoreSongs();
        displayRecommendations(user.getName());
    }

    private User createUser(String username) {
        User user = new User(username);
        musicService.addUser(user);
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