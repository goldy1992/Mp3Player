package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.components.DaggerTestMediaActivityCompatComponent;
import com.example.mike.mp3player.dagger.components.TestMediaActivityCompatComponent;

public class EmptyMediaActivityCompatFragmentActivity extends MediaActivityCompat {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreate(savedInstanceState);
    }
    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.NONE;
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
                .create(getApplicationContext(), getWorkerId(), getSubscriptionType(), this);
        component.inject(this);
        setMediaActivityCompatComponent(component);
    }

    @Override
    boolean initialiseView(int layoutId) {
        return true;
    }
}
