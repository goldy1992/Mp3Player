package com.github.goldy1992.mp3player.dagger.components;

import android.content.Context;

import com.github.goldy1992.mp3player.dagger.modules.AndroidTestContentRetrieverMapModule;
import com.github.goldy1992.mp3player.dagger.modules.PlaybackNotificationManagerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ContentManagerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ContentRetrieverModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ContentSearcherModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ExoPlayerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaItemBuilderModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaItemTypeIdModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaSessionConnectorModule;
import com.github.goldy1992.mp3player.dagger.modules.service.PlaybackManagerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.SearchDatabaseModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ServiceModule;
import com.github.goldy1992.mp3player.service.MediaPlaybackService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AndroidTestContentRetrieverMapModule.class,
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
public interface AndroidTestServiceComponent extends ServiceComponent {

    @Component.Factory
    interface Factory extends ServiceComponent.Factory {
        AndroidTestServiceComponent create(@BindsInstance Context context,
                                @BindsInstance MediaPlaybackService mediaPlaybackService,
                                @BindsInstance String workerId);
    }
}

