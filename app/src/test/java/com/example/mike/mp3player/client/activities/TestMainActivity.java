package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

import com.example.mike.mp3player.dagger.components.DaggerTestMainActivityComponent;

public class TestMainActivity extends MainActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        initialiseDependencies();
        super.onCreate(savedInstance);
    }

    @Override
    void initialiseDependencies() {
        DaggerTestMainActivityComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), getSubscriptionType(), this)
                .inject(this);
    }
}
