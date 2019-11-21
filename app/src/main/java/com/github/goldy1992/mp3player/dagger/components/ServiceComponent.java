package com.github.goldy1992.mp3player.dagger.components;

import android.content.Context;

import com.github.goldy1992.mp3player.dagger.modules.service.ContentManagerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ExoPlayerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaSessionConnectorModule;
import com.github.goldy1992.mp3player.dagger.modules.service.SearchDatabaseModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ServiceModule;
import com.github.goldy1992.mp3player.service.MediaPlaybackService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
    ContentManagerModule.class,
    ExoPlayerModule.class,
    HandlerThreadModule.class,
    MediaSessionCompatModule.class,
    MediaSessionConnectorModule.class,
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
