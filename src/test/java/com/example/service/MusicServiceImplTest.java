package com.example.service;

import com.example.app.model.Song;
import com.example.app.model.User;
import com.example.app.service.MusicServiceImpl;
import com.example.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MusicServiceImplTest {

    @InjectMocks
    private MusicServiceImpl musicService;

    @Mock
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("User1", new HashSet<>(), new HashSet<>());
        when(userService.getUser("User1")).thenReturn(user);
    }

    @Test
    void testAddSong_ValidSong() {
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
    void testAddSongToPlaylist_Success() {
        Song song = new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85);
        musicService.addSong(song);

        musicService.addSongToPlaylist("User1", "Song 1");

        assertTrue(user.getPlaylist().contains(song));
    }

    @Test
    void testAddSongToPlaylist_SongNotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            musicService.addSongToPlaylist("User1", "Unknown Song");
        });
        assertEquals("Song 'Unknown Song' not found.", exception.getMessage());
    }

    @Test
    void testGetRecommendations_NoPlaylist() {
        Song song1 = new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85);
        Song song2 = new Song("Song 2", "Pop", "Artist 2", 200, 2021, 90);
        musicService.addSong(song1);
        musicService.addSong(song2);

        List<Song> recommendations = musicService.getRecommendations("User1");
        assertEquals(2, recommendations.size());
    }

    @Test
    void testGetRecommendations_WithPlaylist() {
        Song song1 = new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85);
        Song song2 = new Song("Song 2", "Pop", "Artist 2", 200, 2021, 90);
        Song song3 = new Song("Song 3", "Rock", "Artist 1", 220, 2020, 80);

        musicService.addSong(song1);
        musicService.addSong(song2);
        musicService.addSong(song3);

        musicService.addSongToPlaylist("User1", "Song 1");

        List<Song> recommendations = musicService.getRecommendations("User1");

        assertEquals(2, recommendations.size());
        assertFalse(recommendations.contains(song1)); // Song 1 should not be in recommendations
        assertTrue(recommendations.contains(song2));  // Song 2 should be recommended
        assertTrue(recommendations.contains(song3));  // Song 3 should be recommended
    }
}

