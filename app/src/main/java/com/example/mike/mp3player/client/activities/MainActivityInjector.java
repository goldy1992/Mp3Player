package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

import com.example.mike.mp3player.dagger.components.DaggerMediaActivityCompatComponent;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;

public class MainActivityInjector extends MainActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        initialiseDependencies();
        super.onCreate(savedInstance);
    }

    /** {@inheritDoc} */
    @Override
    void initialiseDependencies() {
        MediaActivityCompatComponent mediaActivityCompatComponent =
            DaggerMediaActivityCompatComponent
                .factory()
                .create(getApplicationContext(),getWorkerId(), this);
        this.setMediaActivityCompatComponent(mediaActivityCompatComponent);
        mediaActivityCompatComponent.inject(this);


    }
}
