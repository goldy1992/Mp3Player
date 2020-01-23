package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ShuffleMode
import android.view.View
import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import com.github.goldy1992.mp3player.commons.Constants
import javax.inject.Inject

class ShuffleButton

    @Inject
    constructor(context: Context,
                mediaControllerAdapter: MediaControllerAdapter)
    : MediaButton(context, mediaControllerAdapter), PlaybackStateListener {

    @ShuffleMode
    var shuffleMode = 0

    override fun init(view: ImageView) {
        super.init(view)
        mediaControllerAdapter.registerPlaybackStateListener(this)
        updateState(mediaControllerAdapter.shuffleMode)
    }

    fun updateState(@ShuffleMode newState: Int) {
        when (newState) {
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> {
                shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_ALL
                setShuffleOn()
            }
            else -> {
                shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE
                setShuffleOff()
            }
        }
    }

    override fun onClick(view: View?) {
        @ShuffleMode val newShuffleMode: Int
        newShuffleMode = when (shuffleMode) {
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> PlaybackStateCompat.SHUFFLE_MODE_NONE
            else -> PlaybackStateCompat.SHUFFLE_MODE_ALL
        }
        mediaControllerAdapter.shuffleMode = newShuffleMode
    }

    private fun setShuffleOn() {
        setImage(R.drawable.ic_baseline_shuffle_24px, Constants.OPAQUE)
    }

    private fun setShuffleOff() {
        setImage(R.drawable.ic_baseline_shuffle_24px, Constants.TRANSLUCENT)
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        val newShuffleMode = mediaControllerAdapter.shuffleMode
        if (shuffleMode != newShuffleMode) {
            updateState(newShuffleMode)
        }
    }

    companion object {
        private const val LOG_TAG = "SHUFFLE_BTN"
    }
}