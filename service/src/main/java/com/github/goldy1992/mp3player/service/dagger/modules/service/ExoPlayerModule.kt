package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import android.media.MediaMetadataRetriever
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.audio.AudioAttributes
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ExoPlayerModule {
    @Provides
    @ComponentScope
    fun provideExoPlayer(context: Context?): ExoPlayer {
        val simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context)
        val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
        simpleExoPlayer.setAudioAttributes(audioAttributes, true)
        return simpleExoPlayer
    }

    @Provides
    fun providesMediaMetadataRetriever(): MediaMetadataRetriever {
        return MediaMetadataRetriever()
    }
}