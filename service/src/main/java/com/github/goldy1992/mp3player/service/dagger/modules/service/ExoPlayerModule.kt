package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class ExoPlayerModule {

    @Provides
    @ServiceScoped
    fun provideExoPlayer(@ApplicationContext context: Context,
                         renderersFactory: RenderersFactory
    ): Player {
        Log.i("ExoPlayerModule", "providing exoPlayer")
        val exoPlayer = ExoPlayer.Builder(context)
            .setRenderersFactory(renderersFactory)
            .setLooper(context.mainLooper)
            .build()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        exoPlayer.setAudioAttributes(audioAttributes, true)
     //  exoPlayer.addAnalyticsListener(MyAnalyticsListener(mediaSession = m))
        return exoPlayer
    }

    @Provides
    fun providesMediaMetadataRetriever(): MediaMetadataRetriever {
        return MediaMetadataRetriever()
    }




}