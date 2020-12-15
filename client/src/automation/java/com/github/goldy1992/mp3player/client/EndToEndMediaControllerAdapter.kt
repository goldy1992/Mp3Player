package com.github.goldy1992.mp3player.client

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.PlaybackStateCompat

class EndToEndMediaControllerAdapter(context: Context, mediaBrowserCompat: MediaBrowserCompat)
    : MediaControllerAdapter(context, mediaBrowserCompat) {

    var awaitingMediaControllerIdlingResource: AwaitingMediaControllerIdlingResource? = null

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        super.onPlaybackStateChanged(state)

        if (null != awaitingMediaControllerIdlingResource) {
            if (state.state == PlaybackStateCompat.STATE_PLAYING && awaitingMediaControllerIdlingResource!!.isAwaitingPlay()) {
                awaitingMediaControllerIdlingResource!!.stopWaitForPlay()
            }

            if (state.state == PlaybackStateCompat.STATE_PAUSED && awaitingMediaControllerIdlingResource!!.isAwaitingPause()) {
                awaitingMediaControllerIdlingResource!!.stopWaitForPause()
            }
        }
    }

}