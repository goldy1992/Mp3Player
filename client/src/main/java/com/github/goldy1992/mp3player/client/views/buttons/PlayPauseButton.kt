package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import javax.inject.Inject
import javax.inject.Named

class PlayPauseButton @Inject constructor(context: Context, mediaControllerAdapter: MediaControllerAdapter,
                                          @Named("main") mainUpdater: Handler) : MediaButton(context, mediaControllerAdapter, mainUpdater), PlaybackStateListener {
    @get:PlaybackStateCompat.State
    @set:VisibleForTesting
    @PlaybackStateCompat.State
    var state = INITIAL_PLAYBACK_STATE

    override fun init(view: ImageView?) {
        super.init(view)
        updateState(mediaControllerAdapter.playbackState)
        mediaControllerAdapter.registerPlaybackStateListener(this)
    }

    @VisibleForTesting
    override fun onClick(view: View?) {
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            Log.d(LOG_TAG, "calling pause")
            mediaControllerAdapter.pause()
        } else {
            Log.d(LOG_TAG, "calling play")
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
        mainUpdater.post { setPauseIcon() }
        state = PlaybackStateCompat.STATE_PLAYING
    }

    private fun setStatePaused() {
        mainUpdater.post { setPlayIcon() }
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
    private val playIcon: Int
        private get() = R.drawable.ic_baseline_play_arrow_24px

    @get:DrawableRes
    private val pauseIcon: Int
        private get() = R.drawable.ic_baseline_pause_24px

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        updateState(state.state)
    }

    companion object {
        @JvmField
        @PlaybackStateCompat.State
        val INITIAL_PLAYBACK_STATE = PlaybackStateCompat.STATE_STOPPED
        private const val LOG_TAG = "PLAY_PAUSE_BUTTON"
    }
}