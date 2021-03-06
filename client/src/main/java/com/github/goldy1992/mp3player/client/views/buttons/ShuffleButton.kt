package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ShuffleMode
import android.view.View
import androidx.lifecycle.Observer
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.Constants
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ShuffleButton

    @Inject
    constructor(@ActivityContext context: Context,
                mediaControllerAdapter: MediaControllerAdapter)
    : MediaButton(context, mediaControllerAdapter), Observer<Int> {

    @ShuffleMode
    var shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE

    override fun init(imageView: MaterialButton) {
        super.init(imageView)
        updateState(PlaybackStateCompat.SHUFFLE_MODE_NONE)
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
        newShuffleMode = when (shuffleMode)
        {
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> PlaybackStateCompat.SHUFFLE_MODE_NONE
            else -> PlaybackStateCompat.SHUFFLE_MODE_ALL
        }
        mediaControllerAdapter.setShuffleMode(newShuffleMode)
    }

    override fun logTag(): String {
        return "SHUFFLE_BTN"
    }

    private fun setShuffleOn() {
        setImage(R.drawable.ic_baseline_shuffle_24px, Constants.OPAQUE)
    }

    private fun setShuffleOff() {
        setImage(R.drawable.ic_baseline_shuffle_24px, Constants.TRANSLUCENT)
    }

    override fun onChanged(newShuffleMode: Int) {
        if (shuffleMode != newShuffleMode) {
            updateState(newShuffleMode)
        }
    }
}