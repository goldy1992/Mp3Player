package com.github.goldy1992.mp3player.dagger.components;


import android.content.Context;

import com.github.goldy1992.mp3player.MikesMp3Player;
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback;
import com.github.goldy1992.mp3player.dagger.modules.ComponentNameModule;
import com.github.goldy1992.mp3player.dagger.modules.MediaBrowserCompatModule;
import com.github.goldy1992.mp3player.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope;

import dagger.BindsInstance;
import dagger.Component;

@ComponentScope
@Component(modules = {ComponentNameModule.class,
        HandlerThreadModule.class,
        MediaBrowserCompatModule.class
})
public interface ApplicationComponent {

    void inject(MikesMp3Player mikesMp3Player);

    @Component.Factory
    interface Factory {
        ApplicationComponent create(@BindsInstance Context context,
                                            @BindsInstance String workerId,
                                            @BindsInstance MediaBrowserConnectorCallback callback);
    }

}

