package com.github.goldy1992.mp3player.dagger.components;

import android.content.Context;

import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback;
import com.github.goldy1992.mp3player.client.activities.EmptyMediaActivityCompatFragmentActivity;
import com.github.goldy1992.mp3player.dagger.modules.ComponentNameModule;
import com.github.goldy1992.mp3player.dagger.modules.MainHandlerModule;
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserCompatModule;
import com.github.goldy1992.mp3player.dagger.modules.MockAlbumArtPainterModule;
import com.github.goldy1992.mp3player.dagger.modules.MockMediaControllerAdapterModule;
import com.github.goldy1992.mp3player.dagger.modules.MyDrawerListenerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope;

import dagger.BindsInstance;
import dagger.Component;

@ComponentScope
@Component(modules = {
        ComponentNameModule.class,
        MockAlbumArtPainterModule.class,
        HandlerThreadModule.class,
        MainHandlerModule.class,
        MediaBrowserCompatModule.class,
        MockMediaControllerAdapterModule.class,
        MyDrawerListenerModule.class})
public interface TestMediaActivityCompatComponent extends MediaActivityCompatComponent {

    void inject(EmptyMediaActivityCompatFragmentActivity emptyMediaActivityCompatFragmentActivity);

    @Component.Factory
    interface Factory extends MediaActivityCompatComponent.Factory{
        TestMediaActivityCompatComponent create(@BindsInstance Context context,
                                            @BindsInstance String workerId,
                                            @BindsInstance MediaBrowserConnectorCallback callback);

    }
}
