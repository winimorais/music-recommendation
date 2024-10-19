package com.example.app.service;

import com.example.app.model.Song;
import com.example.app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class MusicServiceImpl implements MusicService {

    private final Map<String, Song> songsLibrary = new HashMap<>();
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void addSong(Song song) {
        if (song == null || song.getName() == null || song.getName().isEmpty()) {
            log.error("Invalid song data: song is null or has an empty name.");
            throw new IllegalArgumentException("Song name cannot be null or empty.");
        }

        songsLibrary.put(song.getName(), song);
        log.info("Song '{}' added to the library.", song.getName());
    }

    @Override
    public void addUser(User user) {
        if (user == null || user.getName() == null || user.getName().isEmpty()) {
            log.error("Invalid user data: user is null or has an empty name.");
            throw new IllegalArgumentException("User name cannot be null or empty.");
        }

        users.put(user.getName(), user);
        log.info("User '{}' added.", user.getName());
    }

    @Override
    public void addSongToPlaylist(String username, String songName) {
        User user = validateUser(username);
        Song song = validateSong(songName);

        if (!user.getPlaylist().contains(song)) {
            user.getPlaylist().add(song);
            log.info("Song '{}' added to user '{}' playlist.", songName, username);
        } else {
            log.warn("Song '{}' is already in user '{}' playlist.", songName, username);
        }
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public List<Song> getRecommendations(String username) {
        User user = validateUser(username);
        List<Song> recommendations = new ArrayList<>();

        for (Song song : songsLibrary.values()) {
            if (!user.getPlaylist().contains(song)) {
                recommendations.add(song);
            }
        }

        recommendations.sort((s1, s2) -> Double.compare(
                calculateSimilarityIndex(s2, user), calculateSimilarityIndex(s1, user))
        );

        log.info("Recommendations generated for user '{}', total: {} songs.", username, recommendations.size());
        return recommendations;
    }

    private double calculateSimilarityIndex(Song song, User user) {
        int totalAttributes = 5;
        int totalSimilarities = 0;

        for (Song playlistSong : user.getPlaylist()) {
            int commonAttributes = calculateCommonAttributes(song, playlistSong);
            totalSimilarities += commonAttributes;
        }

        if (user.getPlaylist().isEmpty()) {
            return 0;
        }

        return (double) totalSimilarities / (totalAttributes * user.getPlaylist().size());
    }

    private int calculateCommonAttributes(Song song, Song playlistSong) {
        int commonAttributes = 0;

        if (song.getGenre().equals(playlistSong.getGenre())) commonAttributes++;
        if (song.getTime() == playlistSong.getTime()) commonAttributes++;
        if (song.getSinger().equals(playlistSong.getSinger())) commonAttributes++;
        if (song.getPopularityScore() == playlistSong.getPopularityScore()) commonAttributes++;
        if (song.getReleaseYear() == playlistSong.getReleaseYear()) commonAttributes++;

        return commonAttributes;
    }

    private User validateUser(String username) {
        User user = users.get(username);
        if (user == null) {
            log.error("User '{}' not found.", username);
            throw new IllegalArgumentException("User '" + username + "' not found.");
        }
        return user;
    }

    private Song validateSong(String songName) {
        Song song = songsLibrary.get(songName);
        if (song == null) {
            log.error("Song '{}' not found.", songName);
            throw new IllegalArgumentException("Song '" + songName + "' not found.");
        }
        return song;
    }
}
