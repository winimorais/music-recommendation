package com.example.controller;

import com.example.app.controller.MusicController;
import com.example.app.model.Song;
import com.example.app.model.User;
import com.example.app.service.MusicService;
import com.example.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MusicControllerTest {

    @InjectMocks
    private MusicController musicController;

    @Mock
    private MusicService musicService;

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
        doNothing().when(musicService).addSong(song);

        Map<String, String> songRequest = new HashMap<>();
        songRequest.put("name", song.getName());

        var response = musicController.addSong(song);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Song added to library.", response.getBody());
        verify(musicService, times(1)).addSong(song);
    }

    @Test
    void testAddSong_InvalidSong() {
        Song invalidSong = new Song("", "Rock", "Artist 1", 180, 2022, 85);

        var response = musicController.addSong(invalidSong);

        assertEquals(400, response.getStatusCodeValue());  // Espera que o status seja 400
        assertEquals("Invalid song name.", response.getBody());  // Espera a mensagem de erro correta
        verify(musicService, times(0)).addSong(invalidSong);  // Verifica que o serviço não foi chamado
    }


    @Test
    void testAddSongToPlaylist_Success() {
        Song song = new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85);
        Map<String, String> songRequest = new HashMap<>();
        songRequest.put("name", song.getName());
        doNothing().when(musicService).addSongToPlaylist("User1", song.getName());

        var response = musicController.addSongToPlaylist("User1", songRequest);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Song added to playlist.", response.getBody());
        verify(musicService, times(1)).addSongToPlaylist("User1", song.getName());
    }

    @Test
    void testAddSongToPlaylist_InvalidSong() {
        Map<String, String> songRequest = new HashMap<>();
        songRequest.put("name", "");

        var response = musicController.addSongToPlaylist("User1", songRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid song name.", response.getBody());
        verify(musicService, times(0)).addSongToPlaylist(anyString(), anyString());
    }

    @Test
    void testGetPlaylist_UserFound() {
        user.getPlaylist().add(new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85));

        var response = musicController.getPlaylist("User1");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains(new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85)));
        verify(userService, times(1)).getUser("User1");
    }

    @Test
    void testGetPlaylist_UserNotFound() {
        when(userService.getUser("UnknownUser")).thenReturn(null);

        var response = musicController.getPlaylist("UnknownUser");

        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).getUser("UnknownUser");
    }

    @Test
    void testGetRecommendations() {
        List<Song> recommendations = List.of(new Song("Song 1", "Rock", "Artist 1", 180, 2022, 85));
        when(musicService.getRecommendations("User1")).thenReturn(recommendations);

        var response = musicController.getRecommendations("User1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(recommendations, response.getBody());
        verify(musicService, times(1)).getRecommendations("User1");
    }
}

