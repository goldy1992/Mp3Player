package com.example.mike.mp3player.service;

import androidx.media.MediaBrowserServiceCompat;

public abstract class MediaPlaybackServiceBase extends MediaBrowserServiceCompat {

    abstract void initialiseDependencies();

    @Override
    public void onCreate() {
        initialiseDependencies();
        super.onCreate();
    }


}
