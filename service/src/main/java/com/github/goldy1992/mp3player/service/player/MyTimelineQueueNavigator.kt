package com.github.goldy1992.mp3player.service.player

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import com.github.goldy1992.mp3player.service.PlaylistManager
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class MyTimelineQueueNavigator
    @Inject
    constructor(mediaSession: MediaSessionCompat?,
                val playlistManager: PlaylistManager)
    : TimelineQueueNavigator(mediaSession!!) {

    override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
        val mediaItem = playlistManager.getItemAtIndex(windowIndex)
        return mediaItem!!.description
    }

}