package com.github.goldy1992.mp3player.dagger.components;

import android.content.Context;

import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback;
import com.github.goldy1992.mp3player.dagger.modules.AndroidTestComponentNameModule;
import com.github.goldy1992.mp3player.dagger.modules.GlideModule;
import com.github.goldy1992.mp3player.dagger.modules.MainHandlerModule;
import com.github.goldy1992.mp3player.dagger.modules.MediaBrowserCompatModule;
import com.github.goldy1992.mp3player.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope;

import dagger.BindsInstance;
import dagger.Component;

@ComponentScope
@Component(modules = {
        AndroidTestComponentNameModule.class,
        GlideModule.class,
        HandlerThreadModule.class,
        MainHandlerModule.class,
        MediaBrowserCompatModule.class
})
public interface AndroidTestMediaActivityCompatComponent extends MediaActivityCompatComponent {

    @Component.Factory
    interface Factory extends MediaActivityCompatComponent.Factory{
        AndroidTestMediaActivityCompatComponent create(@BindsInstance Context context,
                                                @BindsInstance String workerId,
                                                @BindsInstance MediaBrowserConnectorCallback callback);

    }
}
