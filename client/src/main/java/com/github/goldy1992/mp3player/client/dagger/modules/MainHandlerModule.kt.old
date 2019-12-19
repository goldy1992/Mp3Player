package com.github.goldy1992.mp3player.client.dagger.modules

import android.os.Handler
import android.os.Looper
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class MainHandlerModule {
    @ComponentScope
    @Provides
    @Named("main")
    fun provideMainHandler(): Handler {
        return Handler(Looper.getMainLooper())
    }
}