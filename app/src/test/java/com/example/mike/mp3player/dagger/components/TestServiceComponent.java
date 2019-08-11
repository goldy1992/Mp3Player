package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.dagger.modules.service.ContentManagerModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.modules.service.MediaItemTypeIdModule;
import com.example.mike.mp3player.dagger.modules.service.MediaPlayerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCallbackModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ContentManagerModule.class,
        HandlerThreadModule.class,
        MediaItemTypeIdModule.class,
        MediaPlayerAdapterModule.class,
        MediaSessionCallbackModule.class,
        MediaSessionCompatModule.class,
        ServiceModule.class })
public interface TestServiceComponent extends ServiceComponent {
    @Component.Factory
    interface Factory extends ServiceComponent.Factory {}
}
