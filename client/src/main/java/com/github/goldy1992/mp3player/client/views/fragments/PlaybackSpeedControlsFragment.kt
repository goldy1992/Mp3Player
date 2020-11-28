package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.databinding.FragmentPlaybackSpeedControlsBinding
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint(Fragment::class)
class PlaybackSpeedControlsFragment : Hilt_PlaybackSpeedControlsFragment(), LogTagger, Observer<Float> {

    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    private var speed = 1.0f

    lateinit var increaseSpeedButton : MaterialButton

    lateinit var decreaseSpeedButton : MaterialButton

    lateinit var currentSpeedTextView : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentPlaybackSpeedControlsBinding.inflate(layoutInflater)
        this.decreaseSpeedButton = binding.decreasePlaybackSpeedButton
        this.increaseSpeedButton = binding.increasePlaybackSpeedButton
        decreaseSpeedButton.setOnClickListener { decreasePlaybackSpeed() }
        increaseSpeedButton.setOnClickListener { increasePlaybackSpeed() }
        this.currentSpeedTextView = binding.playbackSpeedTextView
        mediaControllerAdapter.playbackSpeed.observe(viewLifecycleOwner, this)
        return binding.root
    }

    override fun logTag(): String {
        return "PLY_SPD_CTRL_FGMT"
    }

    private fun updatePlaybackSpeedText(speed: Float) {
        currentSpeedTextView.text = getString(R.string.PLAYBACK_SPEED_VALUE, speed)
    }

    @VisibleForTesting
    fun increasePlaybackSpeed() {
        mediaControllerAdapter.sendCustomAction(Constants.INCREASE_PLAYBACK_SPEED, Bundle())
    }

    @VisibleForTesting
    fun decreasePlaybackSpeed() {
          mediaControllerAdapter.sendCustomAction(Constants.DECREASE_PLAYBACK_SPEED, Bundle())
    }

    override fun onChanged(playbackSpeed: Float?) {
        speed = playbackSpeed!!
        if (speed > 0) {
            updatePlaybackSpeedText(speed)
        }
    }

}