package com.github.goldy1992.mp3player.client.testsupport

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ShuffleMode
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import java.util.*
import javax.inject.Inject

class MockMediaControllerAdapter
    @Inject
    constructor(context: Context?,
                myMediaControllerCallback: MyMediaControllerCallback?)
    : MediaControllerAdapter(context!!, myMediaControllerCallback) {
    override fun setMediaToken(token: MediaSessionCompat.Token?) { // DO NOTHING
    }

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

    override fun registerMetaDataListener(metaDataListener: MetadataListener?) { // DO NOTHING
    }

    override fun unregisterMetaDataListener(metaDataListener: MetadataListener?) { // DO NOTHING
    }

    override fun registerPlaybackStateListener(playbackStateListener: PlaybackStateListener?) { // DO NOTHING
    }

    override val playbackState: Int
        get() = PlaybackStateCompat.STATE_NONE

    override val playbackStateCompat: PlaybackStateCompat
        get() = PlaybackStateCompat.Builder().setState(playbackState, 0L, 0f).build()

    override val metadata: MediaMetadataCompat
        get() = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "artist")
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "title")
                .build()

    override val queue: List<MediaSessionCompat.QueueItem>
        get() = ArrayList()

    override val activeQueueItemId: Long
        get() = 0L

    // DO NOTHING
    @get:ShuffleMode
    override var shuffleMode: Int
        get() = PlaybackStateCompat.SHUFFLE_MODE_ALL
        set(shuffleMode) { // DO NOTHING
        }

    // DO NOTHING
    @get:PlaybackStateCompat.RepeatMode
    override var repeatMode: Int
        get() = PlaybackStateCompat.REPEAT_MODE_ALL
        set(repeatMode) { // DO NOTHING
        }

    override var token: MediaSessionCompat.Token?
        get() = null
        set(token) {
            super.token = token
        }

    override fun disconnect() { // DO NOTHING
    }

    override val isInitialized: Boolean
        get() = true

    override fun sendCustomAction(customAction: String?, args: Bundle?) { // DO NOTHING
    }
}