package com.example.app.controller;

import com.example.app.model.Song;
import com.example.app.model.User;
import com.example.app.service.MusicService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class MusicController {

    @Autowired
    private MusicService musicService;

    // Add a song to the music library
    @PostMapping("/songs")
    public ResponseEntity<String> addSong(@Valid @RequestBody Song song) {
        log.info("Adding song: {}", song.getName());
        musicService.addSong(song);
        return buildResponse(201, "Song added to library.");
    }

    // Add a user
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@Valid @RequestBody User user) {
        log.info("Adding user: {}", user.getName());
        musicService.addUser(user);
        return buildResponse(201, "User added.");
    }

    // Add a song to a user's playlist
    @PostMapping("/users/{username}/playlist")
    public ResponseEntity<String> addSongToPlaylist(
            @Valid @PathVariable String username, @RequestBody Map<String, String> songRequest) {
        String songName = songRequest.get("name");

        if (isInvalidSongName(songName)) {
            log.error("Invalid song name in request body.");
            return buildResponse(400, "Invalid song name.");
        }

        log.info("Adding song '{}' to user '{}' playlist.", songName, username);
        musicService.addSongToPlaylist(username, songName);
        return buildResponse(201, "Song added to playlist.");
    }

    // Retrieve a user's playlist
    @GetMapping("/users/{username}/playlist")
    public ResponseEntity<Set<Song>> getPlaylist(@PathVariable String username) {
        log.info("Fetching playlist for user '{}'", username);
        User user = musicService.getUser(username);

        if (user != null) {
            log.info("User '{}' found. Returning playlist.", username);
            return ResponseEntity.ok(user.getPlaylist());
        } else {
            log.warn("User '{}' not found.", username);
            return ResponseEntity.notFound().build();
        }
    }

    // Get song recommendations for a user based on similarity index
    @GetMapping("/users/{username}/recommendations")
    public ResponseEntity<List<Song>> getRecommendations(@Valid @PathVariable String username) {
        log.info("Fetching recommendations for user '{}'", username);
        List<Song> recommendations = musicService.getRecommendations(username);
        log.info("Returning {} recommendations for user '{}'.", recommendations.size(), username);
        return ResponseEntity.ok(recommendations);
    }

    // Helper Methods

    private ResponseEntity<String> buildResponse(int statusCode, String message) {
        return ResponseEntity.status(statusCode).body(message);
    }

    private boolean isInvalidSongName(String songName) {
        return songName == null || songName.isEmpty();
    }
}