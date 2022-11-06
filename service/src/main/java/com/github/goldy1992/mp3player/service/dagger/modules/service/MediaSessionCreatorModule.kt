package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import com.github.goldy1992.mp3player.service.MediaSessionCreator

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class MediaSessionCreatorModule {
    private val LOG_TAG = "MEDIA_SESSION_COMPAT"

    @Provides
    @ServiceScoped
    fun provideMediaLibrarySession(@ApplicationContext context: Context): MediaSessionCreator {
        return MediaSessionCreator()
    }

}