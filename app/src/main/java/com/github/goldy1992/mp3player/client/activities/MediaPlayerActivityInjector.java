package com.github.goldy1992.mp3player.client.activities;

import android.os.Bundle;

import com.github.goldy1992.mp3player.dagger.components.DaggerMediaActivityCompatComponent;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;

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
        MediaActivityCompatComponent mediaActivityCompatComponent = DaggerMediaActivityCompatComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), this);
                mediaActivityCompatComponent.inject(this);
        this.setMediaActivityCompatComponent(mediaActivityCompatComponent);
    }
}