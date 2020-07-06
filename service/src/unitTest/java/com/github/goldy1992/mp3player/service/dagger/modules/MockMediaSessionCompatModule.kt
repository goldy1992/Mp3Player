package com.github.goldy1992.mp3player.service.dagger.modules

import android.content.Context
import android.media.session.MediaSession
import android.support.v4.media.session.MediaSessionCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class MockMediaSessionCompatModule {

    @ServiceScoped
    @Provides
    fun providesMockMediaSessionCompat(@ApplicationContext context: Context) : MediaSessionCompat {
        val mediaSession = MediaSession(context, "sd")
        val sessionToken = mediaSession.sessionToken
        return MediaSessionCompat.fromMediaSession(context, mediaSession)
    }

    @ServiceScoped
    @Provides
    fun providesMockMediaSessionCompatToken(mediaSessionCompat: MediaSessionCompat) : MediaSessionCompat.Token {
        return mediaSessionCompat.sessionToken
    }

    private fun getMediaSessionCompatToken(context: Context): MediaSessionCompat.Token {
        val mediaSession = MediaSession(context, "sd")
        val sessionToken = mediaSession.sessionToken
        return MediaSessionCompat.Token.fromToken(sessionToken)
    }
}