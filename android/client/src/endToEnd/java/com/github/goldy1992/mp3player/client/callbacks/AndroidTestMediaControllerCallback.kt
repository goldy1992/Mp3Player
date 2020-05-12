package com.github.goldy1992.mp3player.client.callbacks

import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.AwaitingMediaControllerIdlingResource
import com.github.goldy1992.mp3player.client.callbacks.metadata.MyMetadataCallback
import com.github.goldy1992.mp3player.client.callbacks.playback.MyPlaybackStateCallback
import com.github.goldy1992.mp3player.client.callbacks.queue.MyQueueCallback

class AndroidTestMediaControllerCallback(myMetadataCallback: MyMetadataCallback,
                                         myPlaybackStateCallback: MyPlaybackStateCallback,
                                         myQueueCallback: MyQueueCallback)
    : MyMediaControllerCallback(myMetadataCallback, myPlaybackStateCallback, myQueueCallback)  {

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