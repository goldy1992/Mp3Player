package com.example.mike.mp3player.client.view;

public interface SongSearchActionListener {

    void onStartSearch();
    void onFinishSearch();
    void onNewSearchFilter(String filter);
}
