package com.example.service;

import com.example.app.model.Song;
import com.example.app.model.User;
import com.example.app.service.MusicServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class MusicServiceImplTest {

    private MusicServiceImpl musicService;

    @BeforeEach
    void setUp() {
        musicService = new MusicServiceImpl();
    }

    @Test
    void testAddSong_ValidSong() {
        User user = new User("User1", new HashSet<>(), new HashSet<>());
        musicService.addUser(user);

        Song song = new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85);
        musicService.addSong(song);

        assertNotNull(musicService.getRecommendations("User1"));
    }

    @Test
    void testAddSong_InvalidSong() {
        Song invalidSong = new Song("", "Rock", "Artist 1", 180, 2022, 85);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            musicService.addSong(invalidSong);
        });
        assertEquals("Song name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testAddUser_ValidUser() {
        User user = new User("User1", new HashSet<>(), new HashSet<>());
        musicService.addUser(user);
        assertNotNull(musicService.getUser("User1"));
    }

    @Test
    void testAddUser_InvalidUser() {
        User invalidUser = new User("", new HashSet<>(), new HashSet<>());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            musicService.addUser(invalidUser);
        });
        assertEquals("User name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testAddSongToPlaylist_UserNotFound() {
        Song song = new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85);
        musicService.addSong(song);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            musicService.addSongToPlaylist("UnknownUser", "Song 1");
        });
        assertEquals("User 'UnknownUser' not found.", exception.getMessage());
    }

    @Test
    void testAddSongToPlaylist_SongNotFound() {
        User user = new User("User1", new HashSet<>(), new HashSet<>());
        musicService.addUser(user);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            musicService.addSongToPlaylist("User1", "UnknownSong");
        });
        assertEquals("Song 'UnknownSong' not found.", exception.getMessage());
    }

    @Test
    void testGetRecommendations_NoPlaylist() {
        User user = new User("User1", new HashSet<>(), new HashSet<>());
        musicService.addUser(user);
        Song song1 = new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85);
        Song song2 = new Song("Song 2", "Pop", "Artist 2", 200, 2021, 90);
        musicService.addSong(song1);
        musicService.addSong(song2);

        var recommendations = musicService.getRecommendations("User1");
        assertEquals(2, recommendations.size());
    }

    @Test
    void testGetRecommendations_WithPlaylist() {
        User user = new User("User1", new HashSet<>(), new HashSet<>());
        musicService.addUser(user);
        Song song1 = new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85);
        Song song2 = new Song("Song 2", "Pop", "Artist 2", 200, 2021, 90);
        Song song3 = new Song("Song 3", "Rock", "Artist 1", 220, 2020, 80);
        musicService.addSong(song1);
        musicService.addSong(song2);
        musicService.addSong(song3);

        musicService.addSongToPlaylist("User1", "Song 1");
        var recommendations = musicService.getRecommendations("User1");

        assertEquals(2, recommendations.size());
        assertFalse(recommendations.contains(song1)); // Song 1 should not be in recommendations
        assertTrue(recommendations.contains(song2));  // Song 2 should be recommended
        assertTrue(recommendations.contains(song3));  // Song 3 should be recommended
    }
}
