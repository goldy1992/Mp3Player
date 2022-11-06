package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import android.media.MediaMetadataRetriever
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import com.github.goldy1992.mp3player.service.MyForwardingPlayer
import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver
import com.github.goldy1992.mp3player.service.player.MyAnalyticsListener
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
                         analyticsListener: MyAnalyticsListener,
                         renderersFactory: RenderersFactory
    ): ExoPlayer {

        val exoPlayer = ExoPlayer.Builder(context)
            .setRenderersFactory(renderersFactory)
            .build()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        exoPlayer.setAudioAttributes(audioAttributes, true)
        exoPlayer.addAnalyticsListener(analyticsListener)

        return exoPlayer
    }

    @Provides
    @ServiceScoped
    fun provideForwardingPlayer(exoPlayer: ExoPlayer,
                                audioBecomingNoisyBroadcastReceiver: AudioBecomingNoisyBroadcastReceiver
    ) : MyForwardingPlayer {
        return MyForwardingPlayer(exoPlayer, audioBecomingNoisyBroadcastReceiver)
    }

    @Provides
    fun providesMediaMetadataRetriever(): MediaMetadataRetriever {
        return MediaMetadataRetriever()
    }




}