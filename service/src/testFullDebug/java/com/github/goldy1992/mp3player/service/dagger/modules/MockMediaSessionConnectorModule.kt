package com.github.goldy1992.mp3player.service.dagger.modules

import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import org.mockito.kotlin.mock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class MockMediaSessionConnectorModule {
    @Provides
    @ServiceScoped
    fun providesMediaSessionConnector(): MediaSessionConnector {
        return mock<MediaSessionConnector>()
    }
}