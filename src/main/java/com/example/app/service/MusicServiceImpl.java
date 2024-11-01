package com.example.app.service;

import com.example.app.model.Song;
import com.example.app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MusicServiceImpl implements MusicService {

    private final Map<String, Song> songsLibrary = new HashMap<>();
    @Autowired
    private UserService userService;

    @Override
    public void addSong(Song song) {
        validateSongInput(song);
        songsLibrary.put(song.getName(), song);
        log.info("Song '{}' added to the library.", song.getName());
    }

    @Override
    public void addSongToPlaylist(String username, String songName) {
        User user = userService.getUser(username);
        Song song = validateSong(songName);

        if (!user.getPlaylist().contains(song)) {
            user.getPlaylist().add(song);
            log.info("Song '{}' added to user '{}' playlist.", songName, username);
        } else {
            log.warn("Song '{}' is already in user '{}' playlist.", songName, username);
        }
    }

    @Override
    public List<Song> getRecommendations(String username) {
        User user = userService.getUser(username);
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

    private double calculateSimilarityIndex(Song song, User user) {
        int totalAttributes = 5;
        int totalSimilarities = 0;

        for (Song playlistSong : user.getPlaylist()) {
            int commonAttributes = calculateCommonAttributes(song, playlistSong);
            totalSimilarities += commonAttributes;
            System.out.println("song name: " + song.getName() + " | common attributes with '" + playlistSong.getName() + "': " + commonAttributes);
        }

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

    private Song validateSong(String songName) {
        Song song = songsLibrary.get(songName);
        if (song == null) {
            log.error("Song '{}' not found.", songName);
            throw new IllegalArgumentException("Song '" + songName + "' not found.");
        }
        return song;
    }
}