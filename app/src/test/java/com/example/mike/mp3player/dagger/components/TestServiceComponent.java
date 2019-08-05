package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.dagger.modules.MockMediaLibraryModule;
import com.example.mike.mp3player.dagger.modules.MockMediaRetrieverModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.modules.service.MediaLibraryModule;
import com.example.mike.mp3player.dagger.modules.service.MediaPlayerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCallbackModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        HandlerThreadModule.class,
        MockMediaLibraryModule.class,
        MediaPlayerAdapterModule.class,
        MockMediaRetrieverModule.class,
        MediaSessionCallbackModule.class,
        MediaSessionCompatModule.class,
        ServiceModule.class })
public interface TestServiceComponent extends ServiceComponent {
    @Component.Factory
    interface Factory extends ServiceComponent.Factory {}
}
