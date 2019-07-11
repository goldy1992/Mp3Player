package com.example.mike.mp3player.client.activities;

import android.os.Bundle;
import com.example.mike.mp3player.dagger.components.DaggerTestMediaPlayerActivityComponent;

public class MediaPlayerActivityInjectorTestImpl extends MediaPlayerActivity {
    @Override
    public void onCreate(Bundle savedInstance) {
        initialiseDependencies();
        super.onCreate(savedInstance);
    }
    @Override
    void initialiseDependencies() {
        MediaPlayerActivityComponent component = DaggerTestMediaPlayerActivityComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), getSubscriptionType(), this);
                component.inject(this);
        this.setMediaPlayerActivityComponent(component);
    }
}