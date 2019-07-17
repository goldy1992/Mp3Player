package com.example.mike.mp3player.service;

import com.example.mike.mp3player.dagger.components.DaggerServiceComponent;

public class MediaPlaybackServiceInjector extends MediaPlaybackService
{
    @Override
    public void onCreate() {
        initialiseDependencies();
        super.onCreate();
    }

    /**
     * TO BE CALLED BEFORE SUPER CLASS
     */
    @Override
    void initialiseDependencies() {
        DaggerServiceComponent
                .factory()
                .create(getApplicationContext(), this, "MEDIA_PLYBK_SRVC_WKR")
                .inject(this);
    }
}
