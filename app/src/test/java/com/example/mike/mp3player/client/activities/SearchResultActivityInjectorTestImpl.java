package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

import com.example.mike.mp3player.dagger.components.DaggerTestMediaActivityCompatComponent;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;

public class SearchResultActivityInjectorTestImpl extends SearchResultActivity {
    @Override
    public void onCreate(Bundle savedInstance) {
        initialiseDependencies();
        super.onCreate(savedInstance);
    }
    @Override
    void initialiseDependencies() {
        MediaActivityCompatComponent component = DaggerTestMediaActivityCompatComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), getSubscriptionType(), this);
        this.setMediaActivityCompatComponent(component);
        component.inject(this);
    }
}
