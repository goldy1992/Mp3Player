package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class MediaSessionCompatModule {
    private val LOG_TAG = "MEDIA_SESSION_COMPAT"

    @Provides
    @ServiceScoped
    fun provideMediaSessionCompat(@ApplicationContext context: Context): MediaSessionCompat {
        return MediaSessionCompat(context, LOG_TAG)
    }

    @ServiceScoped
    @Provides
    fun provideMediaSessionToken(mediaSessionCompat: MediaSessionCompat): MediaSessionCompat.Token {
        return mediaSessionCompat.sessionToken
    }

}