package com.example.app.service;
import com.example.app.model.Song;
import java.util.List;

public interface MusicService {

    void addSong(Song song);

    void addSongToPlaylist(String username, String songName);

    List<Song> getRecommendations(String username);

}
