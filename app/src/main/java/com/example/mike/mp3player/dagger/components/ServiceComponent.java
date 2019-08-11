package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.dagger.modules.service.ContentManagerModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.modules.service.MediaItemTypeIdModule;
import com.example.mike.mp3player.dagger.modules.service.MediaPlayerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCallbackModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceModule;
import com.example.mike.mp3player.service.MediaPlaybackService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
    ContentManagerModule.class,

    HandlerThreadModule.class,
    MediaPlayerAdapterModule.class,
    MediaSessionCallbackModule.class,
    MediaSessionCompatModule.class,
    MediaItemTypeIdModule.class,
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
