package com.github.goldy1992.mp3player.service;

import com.github.goldy1992.mp3player.dagger.components.AndroidTestServiceComponent;
import com.github.goldy1992.mp3player.dagger.components.DaggerAndroidTestServiceComponent;

public class MediaPlaybackServiceAndroidTestImpl extends MediaPlaybackService {

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
        AndroidTestServiceComponent component = DaggerAndroidTestServiceComponent
                .factory()
                .create(getApplicationContext(), this, "MEDIA_PLYBK_SRVC_WKR");
        component.inject(this);
    }
}