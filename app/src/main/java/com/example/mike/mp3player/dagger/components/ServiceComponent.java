package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.dagger.modules.PlaybackNotificationManagerModule;
import com.example.mike.mp3player.dagger.modules.service.ContentManagerModule;
import com.example.mike.mp3player.dagger.modules.service.ContentRetrieverModule;
import com.example.mike.mp3player.dagger.modules.service.ContentSearcherModule;
import com.example.mike.mp3player.dagger.modules.service.ExoPlayerModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.modules.service.MediaItemBuilderModule;
import com.example.mike.mp3player.dagger.modules.service.MediaItemTypeIdModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionConnectorModule;
import com.example.mike.mp3player.dagger.modules.service.PlaybackManagerModule;
import com.example.mike.mp3player.dagger.modules.service.SearchDatabaseModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceModule;
import com.example.mike.mp3player.service.MediaPlaybackService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
    ContentManagerModule.class,
    ContentRetrieverModule.class,
    ContentSearcherModule.class,
    ExoPlayerModule.class,
    HandlerThreadModule.class,
    MediaItemBuilderModule.class,
    MediaItemTypeIdModule.class,
    MediaSessionCompatModule.class,
    MediaSessionConnectorModule.class,
    PlaybackManagerModule.class,
    PlaybackNotificationManagerModule.class,
    SearchDatabaseModule.class,
    ServiceModule.class
})
public interface ServiceComponent {

    void inject(MediaPlaybackService mediaPlaybackService);

    @Component.Factory
    interface Factory {
        ServiceComponent create(@BindsInstance Context context,
                                @BindsInstance MediaPlaybackService mediaPlaybackService,
                                @BindsInstance String workerId);
    }
}
