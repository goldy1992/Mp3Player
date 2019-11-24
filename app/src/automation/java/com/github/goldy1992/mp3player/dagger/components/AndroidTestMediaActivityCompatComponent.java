package com.github.goldy1992.mp3player.dagger.components;

import android.content.Context;

import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback;
import com.github.goldy1992.mp3player.dagger.modules.AlbumArtPainterModule;
import com.github.goldy1992.mp3player.dagger.modules.AndroidTestComponentNameModule;
import com.github.goldy1992.mp3player.dagger.modules.MainHandlerModule;
import com.github.goldy1992.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.github.goldy1992.mp3player.dagger.modules.MediaControllerCallbackModule;
import com.github.goldy1992.mp3player.dagger.modules.MyDrawerListenerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope;

import dagger.BindsInstance;
import dagger.Component;

@ComponentScope
@Component(modules = {
        AndroidTestComponentNameModule.class,
        AlbumArtPainterModule.class,
        HandlerThreadModule.class,
        MainHandlerModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerAdapterModule.class,
        MediaControllerCallbackModule.class,
        MyDrawerListenerModule.class
})
public interface AndroidTestMediaActivityCompatComponent extends MediaActivityCompatComponent {

    @Component.Factory
    interface Factory extends MediaActivityCompatComponent.Factory{
        AndroidTestMediaActivityCompatComponent create(@BindsInstance Context context,
                                                @BindsInstance String workerId,
                                                @BindsInstance MediaBrowserConnectorCallback callback);

    }
}
