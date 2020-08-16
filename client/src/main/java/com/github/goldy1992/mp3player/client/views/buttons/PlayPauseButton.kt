package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
open class PlayPauseButton
    @Inject
    constructor(@ActivityContext context: Context,
                mediaControllerAdapter: MediaControllerAdapter)
    : MediaButton(context, mediaControllerAdapter), Observer<PlaybackStateCompat> {

    @PlaybackStateCompat.State
    var state = INITIAL_PLAYBACK_STATE

    @VisibleForTesting
    override fun onClick(view: View?) {
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            Log.d(logTag(), "calling pause")
            mediaControllerAdapter.pause()
        } else {
            Log.d(logTag(), "calling play")
            mediaControllerAdapter.play()
        }
    }

    private fun setStatePlaying() {
        setPauseIcon()
        state = PlaybackStateCompat.STATE_PLAYING
    }

    private fun setStatePaused() {
        setPlayIcon()
        state = PlaybackStateCompat.STATE_PAUSED
    }

    @Synchronized
    private fun setPlayIcon() {
        setImage(playIcon)
    }

    @Synchronized
    private fun setPauseIcon() {
        setImage(pauseIcon)
    }

    @get:DrawableRes
    val playIcon = R.drawable.ic_baseline_play_arrow_24px

    @get:DrawableRes
    val pauseIcon = R.drawable.ic_baseline_pause_24px

    companion object {
        @PlaybackStateCompat.State
        const val INITIAL_PLAYBACK_STATE = PlaybackStateCompat.STATE_STOPPED
    }

    override fun logTag(): String {
        return "PLAY_PAUSE_BUTTON";
    }

    /**
     * Called when the data is changed.
     * @param t  The new data
     */
    override fun onChanged(t: PlaybackStateCompat?) {
        when (t?.state) {
            PlaybackStateCompat.STATE_PLAYING -> setStatePlaying()
            else -> setStatePaused()
        }
    }
}