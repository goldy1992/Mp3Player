package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

import com.example.mike.mp3player.dagger.components.DaggerMediaPlayerActivityComponent;

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
        DaggerMediaPlayerActivityComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), getSubscriptionType(), this)
                .inject(this);
    }
}
