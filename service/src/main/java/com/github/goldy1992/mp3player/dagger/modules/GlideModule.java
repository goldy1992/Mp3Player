package com.github.goldy1992.mp3player.dagger.modules;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class GlideModule {

    @ComponentScope
    @Provides
    public RequestManager providesRequestManager(Context context) {
        return Glide.with(context);
    }
}
