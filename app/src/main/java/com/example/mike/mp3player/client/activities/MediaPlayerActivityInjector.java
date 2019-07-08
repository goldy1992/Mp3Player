package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

import com.example.mike.mp3player.dagger.components.DaggerMediaPlayerActivityComponent;
import com.example.mike.mp3player.dagger.components.MediaPlayerActivityComponent;

/**
 * Media Player Activity injector
 */
public class MediaPlayerActivityInjector extends MediaPlayerActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        initialiseDependencies();
        super.onCreate(savedInstance);
    }
    @Override
    void initialiseDependencies() {
        MediaPlayerActivityComponent mainActivityComponent = DaggerMediaPlayerActivityComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), getSubscriptionType(), this);
                mainActivityComponent.inject(this);
                this.setMediaPlayerActivityComponent(mainActivityComponent);
    }
}
