package com.github.goldy1992.mp3player.client

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ShuffleMode
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@ComponentScope
class MockMediaControllerAdapter
    @Inject
    constructor(context: Context?,
                myMediaControllerCallback: MyMediaControllerCallback)
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

    override fun registerListener(listener: Listener) { // DO NOTHING
    }

    override fun removeListener(listener: Listener) {
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

    override fun getQueue(): List<MediaSessionCompat.QueueItem> {
        return ArrayList()
    }

    override fun getActiveQueueItemId(): Long {
        return 0L
    }

    // DO NOTHING
    @ShuffleMode
    override fun getShuffleMode() : Int? {
        return PlaybackStateCompat.SHUFFLE_MODE_ALL
    }

    override fun setShuffleMode(shuffleMode : Int) { // DO NOTHING
    }

    // DO NOTHING
    @PlaybackStateCompat.RepeatMode
    override fun getRepeatMode() : Int {
        return PlaybackStateCompat.REPEAT_MODE_ALL
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

    override val isInitialized: Boolean
        get() = true

    override fun sendCustomAction(customAction: String?, args: Bundle?) { // DO NOTHING
    }
}