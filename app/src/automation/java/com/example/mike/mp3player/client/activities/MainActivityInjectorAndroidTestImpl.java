package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

import androidx.test.espresso.IdlingResource;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mike.mp3player.dagger.components.DaggerAndroidTestMediaActivityCompatComponent;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;

public class MainActivityInjectorAndroidTestImpl extends MainActivity implements IdlingResource {

    private ResourceCallback resourceCallback;

    @Override
    public void onCreate(Bundle savedInstance) {
        initialiseDependencies();
        super.onCreate(savedInstance);

    }

    @Override
    void initialiseDependencies() {
        MediaActivityCompatComponent component = DaggerAndroidTestMediaActivityCompatComponent
                .factory()
                .create(getApplicationContext(), getWorkerId(), this);
        this.setMediaActivityCompatComponent(component);
        component.inject(this);
    }

    @Override
    public String getName() {
        return "mainActivity";
    }

    @Override
    public boolean isIdleNow() {
        if (this.getRootMenuItemsPager() != null) {
            ViewPager2 viewPager2 = this.getRootMenuItemsPager();
            if (viewPager2.getAdapter() != null) {
                boolean isIdle = viewPager2.getAdapter().getItemCount() >= 2;
                if (isIdle) {
                    this.resourceCallback.onTransitionToIdle();
                }
                return isIdle;
            }
        }

        return false;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}
