package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.dagger.modules.MockContentManagerModule;
import com.example.mike.mp3player.dagger.modules.MockMediaSessionConnectorModule;
import com.example.mike.mp3player.dagger.modules.PlaybackNotificationManagerModule;
import com.example.mike.mp3player.dagger.modules.service.ContentManagerModule;
import com.example.mike.mp3player.dagger.modules.service.ContentRetrieverModule;
import com.example.mike.mp3player.dagger.modules.service.ContentSearcherModule;
import com.example.mike.mp3player.dagger.modules.service.ExoPlayerModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.modules.service.MediaItemTypeIdModule;
import com.example.mike.mp3player.dagger.modules.service.MediaPlayerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCallbackModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionConnectorModule;
import com.example.mike.mp3player.dagger.modules.service.PlaybackManagerModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        MockContentManagerModule.class,
        ContentRetrieverModule.class,
        ContentSearcherModule.class,
        ExoPlayerModule.class,
        HandlerThreadModule.class,
        MediaItemTypeIdModule.class,
        MediaPlayerAdapterModule.class,
        MockMediaSessionConnectorModule.class,
        MediaSessionCompatModule.class,
        PlaybackManagerModule.class,
        PlaybackNotificationManagerModule.class,
        ServiceModule.class })
public interface TestServiceComponent extends ServiceComponent {
    @Component.Factory
    interface Factory extends ServiceComponent.Factory {}
}
