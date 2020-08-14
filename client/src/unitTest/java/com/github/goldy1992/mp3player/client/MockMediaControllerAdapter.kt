package com.github.goldy1992.mp3player.client

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ShuffleMode
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import javax.inject.Inject

@ComponentScope
class MockMediaControllerAdapter


    @Inject
    constructor(context: Context?,
                mediaBrowserCompat: MediaBrowserCompat)
    : MediaControllerAdapter(context!!, mediaBrowserCompat) {

    override fun prepareFromMediaId(mediaId: String?, extras: Bundle?) { // DO NOTHING
    }

    override fun playFromMediaId(mediaId: String?, extras: Bundle?) { // DO NOTHING
    }

    override fun playFromUri(uri: Uri?, extras: Bundle?) { // DO NOTHING
    }

    override fun play() { // DO NOTHING
    }

    override fun pause() { // DO NOTHING
    }

    override fun seekTo(position: Long) { // DO NOTHING
    }

    override fun stop() { // DO NOTHING
    }

    override fun skipToNext() { // DO NOTHING
    }

    override fun skipToPrevious() { // DO NOTHING
    }


    override val playbackState: MutableLiveData<PlaybackStateCompat> = MutableLiveData()

    override val metadata: MutableLiveData<MediaMetadataCompat> = MutableLiveData()

    override fun getActiveQueueItemId(): Long {
        return 0L
    }

    override fun setShuffleMode(shuffleMode : Int) { // DO NOTHING
    }

    override fun setRepeatMode(repeatMode : Int ) { // DO NOTHING
    }

    override var token: MediaSessionCompat.Token?
        get() = null
        set(token) {
            super.token = token
        }

    override fun disconnect() { // DO NOTHING
    }

    override fun sendCustomAction(customAction: String?, args: Bundle?) { // DO NOTHING
    }
}