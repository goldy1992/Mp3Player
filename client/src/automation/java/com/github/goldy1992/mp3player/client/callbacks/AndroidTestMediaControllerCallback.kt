package com.github.goldy1992.mp3player.client.callbacks

import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.AwaitingMediaControllerIdlingResource
import com.github.goldy1992.mp3player.client.callbacks.metadata.MyMetadataCallback
import com.github.goldy1992.mp3player.client.callbacks.playback.MyPlaybackStateCallback

class AndroidTestMediaControllerCallback(myPlaybackStateCallback: MyPlaybackStateCallback,
                                         myMetadataCallback: MyMetadataCallback,
                                         private val awaitingMediaControllerIdlingResource: AwaitingMediaControllerIdlingResource)
    : MyMediaControllerCallback(myMetadataCallback, myPlaybackStateCallback)  {

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        super.onPlaybackStateChanged(state)


    }
}