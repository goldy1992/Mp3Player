package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.annotation.OptIn as AndroidXOptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@AndroidXOptIn(UnstableApi::class)
@InstallIn(ServiceComponent::class)
@Module
class ExoPlayerModule : LogTagger {

    @Provides
    @ServiceScoped
    fun provideExoPlayer(@ApplicationContext context: Context,
                         renderersFactory: RenderersFactory
    ): Player {
        Log.v(logTag(), "provideExoPlayer() invoked")
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        return ExoPlayer.Builder(context)
            .setRenderersFactory(renderersFactory)
            .setAudioAttributes(audioAttributes, true)
            .build()
    }

    @Provides
    fun providesMediaMetadataRetriever(): MediaMetadataRetriever {
        return MediaMetadataRetriever()
    }

    override fun logTag(): String {
        return "ExoPlayerModule"
    }


}