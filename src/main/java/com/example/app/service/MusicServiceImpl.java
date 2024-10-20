package com.example.app.service;

import com.example.app.model.Song;
import com.example.app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MusicServiceImpl implements MusicService {

    private final Map<String, Song> songsLibrary = new HashMap<>();
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void addSong(Song song) {
        validateSongInput(song);
        songsLibrary.put(song.getName(), song);
        log.info("Song '{}' added to the library.", song.getName());
    }

    @Override
    public void addUser(User user) {
        validateUserInput(user);
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
        return validateUser(username);
    }

    @Override
    public List<Song> getRecommendations(String username) {
        User user = validateUser(username);
        List<Song> recommendations = new ArrayList<>();
        Map<Song, Double> similarityIndexMap = new HashMap<>();

        for (Song song : songsLibrary.values()) {
            if (!user.getPlaylist().contains(song)) {
                recommendations.add(song);
                double similarityIndex = calculateSimilarityIndex(song, user);
                similarityIndexMap.put(song, similarityIndex);
            }
        }

        recommendations.sort((s1, s2) -> Double.compare(similarityIndexMap.get(s2), similarityIndexMap.get(s1)));
        log.info("Recommendations generated for user '{}', total: {} songs.", username, recommendations.size());
        return recommendations;
    }

    private void validateSongInput(Song song) {
        if (song == null || song.getName() == null || song.getName().isEmpty()) {
            log.error("Invalid song data: song is null or has an empty name.");
            throw new IllegalArgumentException("Song name cannot be null or empty.");
        }
    }

    private void validateUserInput(User user) {
        if (user == null || user.getName() == null || user.getName().isEmpty()) {
            log.error("Invalid user data: user is null or has an empty name.");
            throw new IllegalArgumentException("User name cannot be null or empty.");
        }
    }

    private double calculateSimilarityIndex(Song song, User user) {
        int totalAttributes = 5;
        int totalSimilarities = 0;

        for (Song playlistSong : user.getPlaylist()) {
            int commonAttributes = calculateCommonAttributes(song, playlistSong);
            totalSimilarities += commonAttributes;
            System.out.println("song name: " + song.getName() + " | common attributes with '" + playlistSong.getName() + "': " + commonAttributes);        }

        return user.getPlaylist().isEmpty() ? 0 : (double) totalSimilarities / (totalAttributes * user.getPlaylist().size());
    }

    private int calculateCommonAttributes(Song song, Song playlistSong) {
        int commonAttributes = 0;

        if (song.getGenre().equals(playlistSong.getGenre())) commonAttributes++;
        if (song.getTempo() == playlistSong.getTempo()) commonAttributes++;
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