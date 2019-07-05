package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

import com.example.mike.mp3player.dagger.components.DaggerFolderActivityComponent;

public class FolderActivityInjector extends FolderActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        initialiseDependencies();
        super.onCreate(savedInstance);
    }
    @Override
    void initialiseDependencies() {
        DaggerFolderActivityComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), getSubscriptionType(), this)
                .inject(this);
    }
}
