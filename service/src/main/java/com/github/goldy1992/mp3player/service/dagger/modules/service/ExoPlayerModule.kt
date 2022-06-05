package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import android.media.MediaMetadataRetriever
import com.github.goldy1992.mp3player.service.MyForwardingPlayer
import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver
import com.github.goldy1992.mp3player.service.player.MyAnalyticsListener
import com.github.goldy1992.mp3player.service.player.MyPlayerNotificationManager
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.audio.AudioAttributes
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
        renderersFactory: RenderersFactory): ExoPlayer {

        val exoPlayer = ExoPlayer.Builder(context)
            .setRenderersFactory(renderersFactory)
            .build()



        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .build()
        exoPlayer.setAudioAttributes(audioAttributes, true)
        exoPlayer.addAnalyticsListener(analyticsListener)

        return exoPlayer
    }

    @Provides
    @ServiceScoped
    fun provideForwardingPlayer(exoPlayer: ExoPlayer,
                                audioBecomingNoisyBroadcastReceiver: AudioBecomingNoisyBroadcastReceiver,
                                playerNotificationManager: MyPlayerNotificationManager
    ) : MyForwardingPlayer {
        return MyForwardingPlayer(exoPlayer, audioBecomingNoisyBroadcastReceiver, playerNotificationManager)
    }

    @Provides
    fun providesMediaMetadataRetriever(): MediaMetadataRetriever {
        return MediaMetadataRetriever()
    }




}