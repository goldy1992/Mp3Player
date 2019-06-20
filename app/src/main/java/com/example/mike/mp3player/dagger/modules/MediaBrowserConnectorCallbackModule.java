package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaBrowserConnectorCallbackModule {
    private MediaBrowserConnectorCallback mediaBrowserConnectorCallback;

    public MediaBrowserConnectorCallbackModule(MediaBrowserConnectorCallback mediaBrowserConnectorCallback) {
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
    }

    @Singleton
    @Provides
    MediaBrowserConnectorCallback provideMediaBrowserConnectorCallback() {
        return mediaBrowserConnectorCallback;
    }
}
