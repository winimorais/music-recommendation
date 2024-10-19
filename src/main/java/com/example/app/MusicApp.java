package com.example.app;

import com.example.app.model.Song;
import com.example.app.model.User;
import com.example.app.service.MusicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicApp implements CommandLineRunner {

    @Autowired
    private MusicServiceImpl musicService;

    public static void main(String[] args) {
        SpringApplication.run(MusicApp.class, args);
    }

    @Override
    public void run(String... args) {
        // Test 1: Add a user
        User user1 = new User("User1");
        musicService.addUser(user1);

        // Test 2: Add a song
        Song song1 = new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85);
        musicService.addSong(song1);

        // Test 3: Add the song to the user's playlist
        musicService.addSongToPlaylist(user1.getName(), song1.getName());

        // Test 4: Attempt to get recommendations for the user
        var recommendations = musicService.getRecommendations(user1.getName());
        recommendations.forEach(song -> System.out.println(song.getName()));

        // Test 5: Add a second song and check the recommendations again
        Song song2 = new Song("Song 2", "Pop", "Artist 2", 200, 2023, 90);
        musicService.addSong(song2);

        recommendations = musicService.getRecommendations(user1.getName());
        recommendations.forEach(song -> System.out.println(song.getName()));
    }
}

