package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
open class PlayPauseButton
    @Inject
    constructor(@ActivityContext context: Context,
                mediaControllerAdapter: MediaControllerAdapter)
    : MediaButton(context, mediaControllerAdapter), PlaybackStateListener {

    @PlaybackStateCompat.State
    var state = INITIAL_PLAYBACK_STATE

    override fun init(imageView: ImageView) {
        super.init(imageView)
        updateState(mediaControllerAdapter.playbackState)
    }

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

    private fun updateState(newState: Int) {
        if (newState != state) {
            when (newState) {
                PlaybackStateCompat.STATE_PLAYING -> setStatePlaying()
                else -> setStatePaused()
            }
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

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        updateState(state.state)
    }

    companion object {
        @JvmField
        @PlaybackStateCompat.State
        val INITIAL_PLAYBACK_STATE = PlaybackStateCompat.STATE_STOPPED
    }

    override fun logTag(): String {
        return "PLAY_PAUSE_BUTTON";
    }
}