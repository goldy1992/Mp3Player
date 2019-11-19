package com.github.goldy1992.mp3player.dagger.components;

import android.content.Context;

import com.github.goldy1992.mp3player.dagger.modules.service.ContentManagerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ContentRetrieverMapModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ContentRetrieverModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ContentSearcherModule;
import com.github.goldy1992.mp3player.dagger.modules.service.ExoPlayerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaItemBuilderModule;
import com.github.goldy1992.mp3player.dagger.modules.service.MediaItemTypeIdModule;
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
    ContentRetrieverMapModule.class,
    ContentRetrieverModule.class,
    ContentSearcherModule.class,
    ExoPlayerModule.class,
    HandlerThreadModule.class,
    MediaItemBuilderModule.class,
    MediaItemTypeIdModule.class,
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
