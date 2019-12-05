package com.github.goldy1992.mp3player.client.dagger.modules;

import android.content.ComponentName;
import android.content.Context;

import com.github.goldy1992.mp3player.commons.ComponentClassMapper;

import dagger.Module;
import dagger.Provides;

@Module
public class ComponentNameModule {

    /** */
    @Provides
    public ComponentName provideComponentName(Context context, ComponentClassMapper componentClassMapper) {
        return new ComponentName(context, componentClassMapper.getService());
    }
}
