package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaBrowserConnectorCallbackModule {
    private MediaBrowserConnectorCallback mediaBrowserConnectorCallback;

    public MediaBrowserConnectorCallbackModule(MediaBrowserConnectorCallback mediaBrowserConnectorCallback) {
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
    }

    @Provides
    MediaBrowserConnectorCallback provideMediaBrowserConnectorCallback() {
        return mediaBrowserConnectorCallback;
    }
}
