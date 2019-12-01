package com.github.goldy1992.mp3player.client.activities;

import android.os.Bundle;

import com.github.goldy1992.mp3player.dagger.components.DaggerTestMediaActivityCompatComponent;
import com.github.goldy1992.mp3player.dagger.components.TestMediaActivityCompatComponent;

public class EmptyMediaActivityCompatFragmentActivity extends MediaActivityCompat {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreate(savedInstanceState);
    }

    @Override
    String getWorkerId() {
        return "WORKER_ID";
    }

    @Override
    void initialiseDependencies() {
        TestMediaActivityCompatComponent component =
            (TestMediaActivityCompatComponent)
            DaggerTestMediaActivityCompatComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), this);
        component.inject(this);
        mediaActivityCompatComponent = component;
    }

    @Override
    boolean initialiseView(int layoutId) {
        return true;
    }
}
