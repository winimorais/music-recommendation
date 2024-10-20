package com.example.controller;

import com.example.app.controller.MusicController;
import com.example.app.model.Song;
import com.example.app.model.User;
import com.example.app.service.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MusicControllerTest {

    @InjectMocks
    private MusicController musicController;

    @Mock
    private MusicService musicService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddSong() {
        Song song = new Song("Song 1", "Artist", "Pop", 5, 2024, 50);

        ResponseEntity<String> response = musicController.addSong(song);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Song added to library.", response.getBody());
        verify(musicService, times(1)).addSong(song);
    }

    @Test
    public void testAddUser() {
        User user = new User("User 1");

        ResponseEntity<String> response = musicController.addUser(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User added.", response.getBody());
        verify(musicService, times(1)).addUser(user);
    }

    @Test
    public void testAddSongToPlaylist_Valid() {
        String username = "User 1";
        Map<String, String> songRequest = new HashMap<>();
        songRequest.put("name", "Song 1");

        when(musicService.getUser(username)).thenReturn(new User(username));

        ResponseEntity<String> response = musicController.addSongToPlaylist(username, songRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Song added to playlist.", response.getBody());
        verify(musicService, times(1)).addSongToPlaylist(username, "Song 1");
    }

    @Test
    public void testAddSongToPlaylist_InvalidSongName() {
        String username = "User 1";
        Map<String, String> songRequest = new HashMap<>();
        songRequest.put("name", "");

        ResponseEntity<String> response = musicController.addSongToPlaylist(username, songRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid song name.", response.getBody());
        verify(musicService, never()).addSongToPlaylist(any(), any());
    }

    @Test
    public void testGetPlaylist_UserFound() {
        String username = "User 1";
        User user = new User(username);
        user.setPlaylist(new HashSet<>(Arrays.asList(new Song("Song 1", "Artist", "Pop", 5, 2024, 50))));

        when(musicService.getUser(username)).thenReturn(user);

        ResponseEntity<Set<Song>> response = musicController.getPlaylist(username);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(musicService, times(1)).getUser(username);
    }

    @Test
    public void testGetPlaylist_UserNotFound() {
        String username = "User 1";

        when(musicService.getUser(username)).thenReturn(null);

        ResponseEntity<Set<Song>> response = musicController.getPlaylist(username);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(musicService, times(1)).getUser(username);
    }

    @Test
    public void testGetRecommendations() {
        String username = "User 1";
        List<Song> recommendations = Arrays.asList(new Song("Song 2", "Artist", "Pop", 5, 2024, 80));

        when(musicService.getRecommendations(username)).thenReturn(recommendations);

        ResponseEntity<List<Song>> response = musicController.getRecommendations(username);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Song 2", response.getBody().get(0).getName());
        verify(musicService, times(1)).getRecommendations(username);
    }
}
