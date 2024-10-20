package com.example.app.service;

import com.example.app.model.Song;
import com.example.app.model.User;

import java.util.List;

public interface MusicService {

    void addSong(Song song);

    void addUser(User user);

    void addSongToPlaylist(String username, String songName);

    User getUser(String username);

    List<Song> getRecommendations(String username);

}
