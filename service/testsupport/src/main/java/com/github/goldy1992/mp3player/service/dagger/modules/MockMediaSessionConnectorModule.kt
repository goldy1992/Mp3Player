package com.github.goldy1992.mp3player.service.dagger.modules

import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class MockMediaSessionConnectorModule {
    @Provides
    @Singleton
    fun providesMediaSessionConnector(): MediaSessionConnector {
        return Mockito.mock(MediaSessionConnector::class.java)
    }
}