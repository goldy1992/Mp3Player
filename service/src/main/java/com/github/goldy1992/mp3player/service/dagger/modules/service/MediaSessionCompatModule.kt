package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.github.goldy1992.mp3player.service.player.MyMediaButtonEventHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MediaSessionCompatModule {
    private val LOG_TAG = "MEDIA_SESSION_COMPAT"
    @Singleton
    @Provides
    fun provideMediaSessionCompat(context: Context?): MediaSessionCompat {
        return MediaSessionCompat(context, LOG_TAG)
    }

    @Singleton
    @Provides
    fun provideMediaSessionToken(mediaSessionCompat: MediaSessionCompat): MediaSessionCompat.Token {
        return mediaSessionCompat.sessionToken
    }

    @Provides
    @Singleton
    fun myMediaButtonEventHandler(): MyMediaButtonEventHandler {
        return MyMediaButtonEventHandler()
    }
}