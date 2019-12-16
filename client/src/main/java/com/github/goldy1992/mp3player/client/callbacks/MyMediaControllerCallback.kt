package com.github.goldy1992.mp3player.client.callbacks

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.callbacks.metadata.MyMetadataCallback
import com.github.goldy1992.mp3player.client.callbacks.playback.MyPlaybackStateCallback
import javax.inject.Inject

/**
 * Created by Mike on 04/10/2017.
 * TODO: ORGANIZE LISTENERS INTO CATEGORIES DEFINED BY THE ACTION THAT SHOULD BE SET IN THE ACTIONS LIST
 */
class MyMediaControllerCallback @Inject constructor(val myMetaDataCallback: MyMetadataCallback,
                                                    val myPlaybackStateCallback: MyPlaybackStateCallback) : MediaControllerCompat.Callback() {
    override fun onMetadataChanged(metadata: MediaMetadataCompat) {
        myMetaDataCallback.processCallback(metadata)
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        myPlaybackStateCallback.processCallback(state)
    }

}