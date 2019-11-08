package com.github.goldy1992.mp3player.client.activities;

import android.os.Bundle;

import com.github.goldy1992.mp3player.dagger.components.DaggerTestMediaActivityCompatComponent;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;

public class MediaPlayerActivityInjectorTestImpl extends MediaPlayerActivity {
    @Override
    public void onCreate(Bundle savedInstance) {
        initialiseDependencies();
        super.onCreate(savedInstance);
    }
    @Override
    void initialiseDependencies() {
        MediaActivityCompatComponent component = DaggerTestMediaActivityCompatComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), this);
        this.setMediaActivityCompatComponent(component);
                component.inject(this);
    }
}