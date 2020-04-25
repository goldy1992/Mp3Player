package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import com.github.goldy1992.mp3player.commons.Constants

import kotlinx.android.synthetic.main.fragment_playback_speed_controls.*

import javax.inject.Inject

class PlaybackSpeedControlsFragment : MediaFragment(), PlaybackStateListener {

    private var speed = 1.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_playback_speed_controls, container, true)
    }

    override fun logTag(): String {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        decreasePlaybackSpeedButton.setOnClickListener(View.OnClickListener { decreasePlaybackSpeed() })
        increasePlaybackSpeedButton.setOnClickListener(View.OnClickListener { increasePlaybackSpeed() })

        // register listeners
        mediaControllerAdapter.registerListener(this)
        //update GUI
        //onPlaybackStateChanged(mediaControllerAdapter.playbackStateCompat!!)
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

    override fun initialiseDependencies() {
        createMediaFragmentSubcomponent()
        ?.inject(this)
    }

}