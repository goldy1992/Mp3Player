package com.github.goldy1992.mp3player.dagger.components;

import com.github.goldy1992.mp3player.dagger.modules.MockContentManagerModule;
import com.github.goldy1992.mp3player.dagger.modules.MockMediaSessionConnectorModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ExoPlayerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        MockContentManagerModule.class,
        ExoPlayerModule.class,
        HandlerThreadModule.class,
        MockMediaSessionConnectorModule.class,
        MediaSessionCompatModule.class,
        ServiceModule.class })
public interface TestServiceComponent extends ServiceComponent {
    @Component.Factory
    interface Factory extends ServiceComponent.Factory {}
}
