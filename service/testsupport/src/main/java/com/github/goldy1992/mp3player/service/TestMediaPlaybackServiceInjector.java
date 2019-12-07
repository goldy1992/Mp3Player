package com.github.goldy1992.mp3player.service;

import com.github.goldy1992.mp3player.commons.ComponentClassMapper;
import com.github.goldy1992.mp3player.service.MediaPlaybackService;
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent;
import com.github.goldy1992.mp3player.service.dagger.components.TestServiceComponent;
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
    protected void initialiseDependencies() {
       ServiceComponent component =  DaggerTestServiceComponent
                .factory()
                .create(getApplicationContext(), this, "MEDIA_PLYBK_SRVC_WKR", new ComponentClassMapper.Builder().build());
                component.inject(this);
    }
}
