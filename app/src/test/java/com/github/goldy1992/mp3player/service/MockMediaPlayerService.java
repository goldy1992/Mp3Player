package com.github.goldy1992.mp3player.service;

public class MockMediaPlayerService extends MediaPlaybackService {

    @Override
    public void initialiseDependencies() {
        System.out.println("hit fake service");
    }
}
