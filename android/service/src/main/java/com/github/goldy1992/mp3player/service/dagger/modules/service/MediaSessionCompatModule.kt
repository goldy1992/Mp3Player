package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.player.MyMediaButtonEventHandler
import dagger.Module
import dagger.Provides

@Module
class MediaSessionCompatModule {
    private val LOG_TAG = "MEDIA_SESSION_COMPAT"

    @Provides
    @ComponentScope
    fun provideMediaSessionCompat(context: Context): MediaSessionCompat {
        return MediaSessionCompat(context, LOG_TAG)
    }

    @ComponentScope
    @Provides
    fun provideMediaSessionToken(mediaSessionCompat: MediaSessionCompat): MediaSessionCompat.Token {
        return mediaSessionCompat.sessionToken
    }

    @Provides
    @ComponentScope
    fun myMediaButtonEventHandler(): MyMediaButtonEventHandler {
        return MyMediaButtonEventHandler()
    }
}