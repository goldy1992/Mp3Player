package com.example.mike.mp3player.client.view;

public interface MediaPlayerActionListener {
    void playSelectedSong(String songId);
    void play();
    void pause();
    void goToMediaPlayerActivity();
    void skipToNext();
    void skipToPrevious();
}
