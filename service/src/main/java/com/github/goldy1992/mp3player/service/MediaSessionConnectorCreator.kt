package com.github.goldy1992.mp3player.service

import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.commons.Constants.AUDIO_SESSION_ID
import com.github.goldy1992.mp3player.service.player.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector.PlaybackActions
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class MediaSessionConnectorCreator
    @Inject
    constructor(
        private val mediaSessionCompat: MediaSessionCompat,
        private val exoPlayer: ExoPlayer,
        private val myPlaybackPreparer: MyPlaybackPreparer,
     //   private val myMetadataProvider: MyMetadataProvider,
        private val myTimelineQueueNavigator: MyTimelineQueueNavigator,
        private val changeSpeedProvider: ChangeSpeedProvider,
        private val myMediaButtonEventHandler: MyMediaButtonEventHandler,
        private val playlistManager: PlaylistManager) {
    private var mediaSessionConnector: MediaSessionConnector? = null
    fun create(): MediaSessionConnector? {
        if (null == mediaSessionConnector) {

            val newMediaSessionConnector = MediaSessionConnector(mediaSessionCompat)
            mediaSessionConnector = newMediaSessionConnector
            val toSend = Bundle()
            toSend.putInt(AUDIO_SESSION_ID, exoPlayer.audioSessionId)
            mediaSessionCompat.setExtras(toSend)

            if (!playlistManager.isEmpty()) {
                newMediaSessionConnector.setPlaybackPreparer(myPlaybackPreparer)
                newMediaSessionConnector.setMediaMetadataProvider(MediaSessionConnector.DefaultMediaMetadataProvider(mediaSessionCompat.controller, null))
                newMediaSessionConnector.setQueueNavigator(myTimelineQueueNavigator)
                newMediaSessionConnector.setCustomActionProviders(changeSpeedProvider)
                newMediaSessionConnector.setMediaButtonEventHandler(myMediaButtonEventHandler)
                newMediaSessionConnector.setEnabledPlaybackActions(SUPPORTED_PLAYBACK_ACTIONS)
                newMediaSessionConnector.setPlayer(exoPlayer)
            }
        }
        return mediaSessionConnector
    }

    companion object {
        @PlaybackActions
        val SUPPORTED_PLAYBACK_ACTIONS =
            PlaybackStateCompat.ACTION_STOP or
            PlaybackStateCompat.ACTION_PAUSE or
            PlaybackStateCompat.ACTION_PLAY or
            PlaybackStateCompat.ACTION_SET_REPEAT_MODE or
            PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE or
            PlaybackStateCompat.ACTION_SEEK_TO
    }

}