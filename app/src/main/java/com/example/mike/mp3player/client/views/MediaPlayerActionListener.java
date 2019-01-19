package com.example.mike.mp3player.client.views;

import android.os.Bundle;

public interface MediaPlayerActionListener {
    void playSelectedSong(String songId);
    void play();
    void pause();
    void goToMediaPlayerActivity();
    void skipToNext();
    void skipToPrevious();
    void seekTo(int position);
    void sendCustomAction(String customAction, Bundle args);
}
