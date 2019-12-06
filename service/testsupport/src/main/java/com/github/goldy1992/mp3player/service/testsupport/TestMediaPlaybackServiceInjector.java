package com.github.goldy1992.mp3player.service.testsupport;

import com.github.goldy1992.mp3player.service.MediaPlaybackService;
import com.github.goldy1992.mp3player.service.dagger.components.DaggerTestServiceComponent;

public class TestMediaPlaybackServiceInjector extends MediaPlaybackService {

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
        DaggerTestServiceComponent
                .factory()
                .create(getApplicationContext(), this, "MEDIA_PLYBK_SRVC_WKR")
                .inject(this);
    }
}
