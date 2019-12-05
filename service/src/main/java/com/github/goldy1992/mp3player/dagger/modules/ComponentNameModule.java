package com.github.goldy1992.mp3player.dagger.modules;

import android.content.ComponentName;
import android.content.Context;

import com.github.goldy1992.mp3player.service.MediaPlaybackServiceInjector;

import dagger.Module;
import dagger.Provides;

@Module
public class ComponentNameModule {

    /** */
    @Provides
    public ComponentName provideComponentName(Context context) {
        return new ComponentName(context, MediaPlaybackServiceInjector.class);
    }
}
