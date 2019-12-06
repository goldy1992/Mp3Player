package com.github.goldy1992.mp3player.service.dagger.components;

import com.github.goldy1992.mp3player.dagger.modules.MockSearchDatabaseModule;
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent;
import com.github.goldy1992.mp3player.service.dagger.modules.service.ContentManagerModule;
import com.github.goldy1992.mp3player.service.dagger.modules.service.ExoPlayerModule;
import com.github.goldy1992.mp3player.service.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.service.dagger.modules.service.MediaSessionCompatModule;
import com.github.goldy1992.mp3player.service.dagger.modules.service.MediaSessionConnectorModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ContentManagerModule.class,
        ExoPlayerModule.class,
        HandlerThreadModule.class,
        MediaSessionConnectorModule.class,
        MediaSessionCompatModule.class,
        MockSearchDatabaseModule.class })
public interface TestServiceComponent extends ServiceComponent {
    @Component.Factory
    interface Factory extends ServiceComponent.Factory {}
}
