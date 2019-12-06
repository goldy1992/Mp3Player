package com.github.goldy1992.mp3player.commons.dagger.components;

import com.github.goldy1992.mp3player.commons.ComponentClassMapper;
import com.github.goldy1992.mp3player.commons.MikesMp3Player;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component
public interface AppComponent {

    void inject(MikesMp3Player mikesMp3Player);

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance ComponentClassMapper componentClassMapper);
    }
}
