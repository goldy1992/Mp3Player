package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.listeners.MyDrawerListener
import dagger.Module
import dagger.Provides

@Module
class MyDrawerListenerModule {
    @Provides
    fun provideMyDrawerListener(): MyDrawerListener {
        return MyDrawerListener()
    }
}