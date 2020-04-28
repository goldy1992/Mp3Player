package com.github.goldy1992.mp3player.service

import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.player.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector.PlaybackActions
import javax.inject.Inject

@ComponentScope
class MediaSessionConnectorCreator
    @Inject
    constructor(
        private val mediaSessionCompat: MediaSessionCompat,
        private val exoPlayer: ExoPlayer,
        private val myPlaybackPreparer: MyPlaybackPreparer,
        private val myControlDispatcher: MyControlDispatcher,
        private val myMetadataProvider: MyMetadataProvider,
        private val myTimelineQueueNavigator: MyTimelineQueueNavigator,
        private val increaseSpeedProvider: IncreaseSpeedProvider,
        private val decreaseSpeedProvider: DecreaseSpeedProvider,
        private val myMediaButtonEventHandler: MyMediaButtonEventHandler) {
    private var mediaSessionConnector: MediaSessionConnector? = null
    fun create(): MediaSessionConnector? {
        if (null == mediaSessionConnector) {
            val newMediaSessionConnector = MediaSessionConnector(mediaSessionCompat)
            newMediaSessionConnector.setPlayer(exoPlayer)
            newMediaSessionConnector.setPlaybackPreparer(myPlaybackPreparer)
            newMediaSessionConnector.setControlDispatcher(myControlDispatcher)
            newMediaSessionConnector.setMediaMetadataProvider(myMetadataProvider)
            newMediaSessionConnector.setQueueNavigator(myTimelineQueueNavigator)
            newMediaSessionConnector.setCustomActionProviders(increaseSpeedProvider, decreaseSpeedProvider)
            newMediaSessionConnector.setMediaButtonEventHandler(myMediaButtonEventHandler)
            newMediaSessionConnector.setEnabledPlaybackActions(SUPPORTED_PLAYBACK_ACTIONS)
            mediaSessionConnector = newMediaSessionConnector
        }
        return mediaSessionConnector
    }

    companion object {
        @PlaybackActions
        val SUPPORTED_PLAYBACK_ACTIONS = PlaybackStateCompat.ACTION_STOP or PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_PLAY or
                PlaybackStateCompat.ACTION_SET_REPEAT_MODE or PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE or PlaybackStateCompat.ACTION_SEEK_TO
    }

}