package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import com.github.goldy1992.mp3player.commons.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_playback_speed_controls.*
import java.util.*

@AndroidEntryPoint
class PlaybackSpeedControlsFragment : MediaFragment(), PlaybackStateListener {

    private var speed = 1.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_playback_speed_controls, container, true)
    }

    override fun logTag(): String {
        return "PLY_SPD_CTRL_FGMT"
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return Collections.singleton(this)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        decreasePlaybackSpeedButton.setOnClickListener(View.OnClickListener { decreasePlaybackSpeed() })
        increasePlaybackSpeedButton.setOnClickListener(View.OnClickListener { increasePlaybackSpeed() })
    }

    private fun updatePlaybackSpeedText(speed: Float) {
        playbackSpeedTextView!!.text = getString(R.string.PLAYBACK_SPEED_VALUE, speed)
    }

    @VisibleForTesting
    fun increasePlaybackSpeed() {
        mediaControllerAdapter.sendCustomAction(Constants.INCREASE_PLAYBACK_SPEED, Bundle())
    }

    @VisibleForTesting
    fun decreasePlaybackSpeed() {
          mediaControllerAdapter.sendCustomAction(Constants.DECREASE_PLAYBACK_SPEED, Bundle())
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        speed = state.playbackSpeed
        if (speed > 0) {
            updatePlaybackSpeedText(speed)
        }
    }

}