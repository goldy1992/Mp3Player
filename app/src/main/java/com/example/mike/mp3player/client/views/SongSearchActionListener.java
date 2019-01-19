package com.example.mike.mp3player.client.views;

public interface SongSearchActionListener {

    void onStartSearch();
    void onFinishSearch();
    void onNewSearchFilter(String filter);
}
