package com.github.goldy1992.mp3player.client

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import java.util.*

class MockMediaControllerAdapter(context: Context, mediaControllerCallback: MyMediaControllerCallback)
    : MediaControllerAdapter(context, null, mediaControllerCallback) {

    override fun prepareFromMediaId(mediaId: String?, extras: Bundle?) {
        // Do nothing.
    }

    override fun playFromMediaId(mediaId: String?, extras: Bundle?) {
        // Do nothing.
    }

    override fun playFromUri(uri: Uri?, extras: Bundle?) {
        // Do nothing.
    }

    override fun play() {
        // Do nothing.
    }

    override fun pause() {
        // Do nothing.
    }

    override fun seekTo(position: Long) {
        // Do nothing.
    }

    override fun stop() {
        // Do nothing.
    }

    override fun skipToNext() {
        // Do nothing.
    }

    override fun skipToPrevious() {
        // Do nothing.
    }

    override val playbackState: Int
        get() = 0

    override val playbackStateCompat: PlaybackStateCompat?
        get() = PlaybackStateCompat.Builder()
                .build()

    override val metadata : MediaMetadataCompat?
        get() = MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 3234L)
                .build() // MediaMetadataCompat.fromMediaMetadata(0)

    override fun getShuffleMode(): Int? {
        return PlaybackStateCompat.SHUFFLE_MODE_ALL
    }

    override fun setShuffleMode(shuffleMode: Int) {
        // Do nothing.
    }

    override fun getRepeatMode(): Int? {
        return PlaybackStateCompat.REPEAT_MODE_ALL
    }

    override fun setRepeatMode(repeatMode: Int) {
        // Do nothing.
    }

    override val isInitialized: Boolean
        get() = true

    override fun sendCustomAction(customAction: String?, args: Bundle?) {
        // Do nothing.
    }

    override fun getQueue(): List<MediaSessionCompat.QueueItem>? {
        return Collections.emptyList()
    }

    override fun getActiveQueueItemId(): Long? {
        return 0L
    }

    override fun getCurrentQueuePosition(): Int {
        return 0
    }
}