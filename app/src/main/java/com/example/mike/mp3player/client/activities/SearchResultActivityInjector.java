package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

import com.example.mike.mp3player.dagger.components.DaggerMediaActivityCompatComponent;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;

public class SearchResultActivityInjector extends SearchResultActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreate(savedInstanceState);
    }

    @Override
    void initialiseDependencies() {
        MediaActivityCompatComponent component = DaggerMediaActivityCompatComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), getSubscriptionType(), this);
        this.setMediaActivityCompatComponent(component);
        component.searchResultActivitySubComponent().inject(this);
    }
}
