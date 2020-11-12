package com.github.goldy1992.mp3player.client

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import dagger.hilt.android.qualifiers.ApplicationContext

class MockMediaControllerAdapter(@ApplicationContext context: Context,
mediaBrowserCompat: MediaBrowserCompat)
    : MediaControllerAdapter(context, mediaBrowserCompat) {

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



    override fun setShuffleMode(shuffleMode: Int) {
        // Do nothing.
    }

    override fun setRepeatMode(repeatMode: Int) {
        // Do nothing.
    }

    override fun sendCustomAction(customAction: String?, args: Bundle?) {
        // Do nothing.
    }

    override fun getActiveQueueItemId() : Long? {
        return 0L
    }

    override fun calculateCurrentQueuePosition() : Int {
        return 0
    }
}