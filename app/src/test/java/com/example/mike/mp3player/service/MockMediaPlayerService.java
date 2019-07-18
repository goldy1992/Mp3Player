package com.example.mike.mp3player.service;

public class MockMediaPlayerService extends MediaPlaybackService {

    @Override
    public void initialiseDependencies() {
        System.out.println("hit fake service");
    }
}
