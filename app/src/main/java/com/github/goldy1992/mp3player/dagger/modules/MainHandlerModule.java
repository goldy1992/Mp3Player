package com.github.goldy1992.mp3player.dagger.modules;

import android.os.Handler;
import android.os.Looper;

import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MainHandlerModule {

    @ComponentScope
    @Provides
    @Named("main")
    public Handler provideMainHandler() {
        return new Handler(Looper.getMainLooper());
    }
}
