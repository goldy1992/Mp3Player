package com.github.goldy1992.mp3player.dagger.components;

import android.content.Context;

import com.github.goldy1992.mp3player.commons.ComponentClassMapper;
import com.github.goldy1992.mp3player.dagger.modules.service.ContentManagerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ExoPlayerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaSessionConnectorModule;
import com.github.goldy1992.mp3player.dagger.modules.service.SearchDatabaseModule;
import com.github.goldy1992.mp3player.service.MediaPlaybackService;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;

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
    SearchDatabaseModule.class
})
public interface ServiceComponent {

    void inject(MediaPlaybackService mediaPlaybackService);

    @Component.Factory
    interface Factory {
        ServiceComponent create(@BindsInstance Context context,
                                @BindsInstance NotificationListener notificationListener,
                                @BindsInstance String workerId,
                                @BindsInstance ComponentClassMapper componentClassMapper);
    }
}
