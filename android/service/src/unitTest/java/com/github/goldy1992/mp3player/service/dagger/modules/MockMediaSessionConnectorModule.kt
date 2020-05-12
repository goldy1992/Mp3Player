package com.github.goldy1992.mp3player.service.dagger.modules

import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class MockMediaSessionConnectorModule {
    @Provides
    @ComponentScope
    fun providesMediaSessionConnector(): MediaSessionConnector {
        return mock<MediaSessionConnector>()
    }
}