package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.components.DaggerMainActivityComponent;
import com.example.mike.mp3player.dagger.components.MainActivityComponent;

public class MainActivityProduction extends MainActivity {
    @Override
    public void onCreate(Bundle savedInstance) {
        initialiseDependencies();
        super.onCreate(savedInstance);
    }

    /** {@inheritDoc} */
    @Override
    void initialiseDependencies() {
        MainActivityComponent mainActivityComponent = DaggerMainActivityComponent
                .factory()
                .create(getApplicationContext(),getWorkerId(), SubscriptionType.MEDIA_ID, this);
        mainActivityComponent.inject(this);

    }
}
