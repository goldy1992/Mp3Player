package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.SeekerBarController2
import com.github.goldy1992.mp3player.client.databinding.FragmentPlaybackTrackerBinding
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.client.views.TimeCounter
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackTrackerFragment : Fragment(), LogTagger, Observer<Any> {

    @Inject
    lateinit var mediaControllerAdapter : MediaControllerAdapter

    @Inject
    lateinit var seekerBarController: SeekerBarController2

    @Inject
    lateinit var counter: TimeCounter

    private var duration: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentPlaybackTrackerBinding.inflate(inflater)
        val counterView = binding.timer
        counter.init(counterView)
        val seekerBar: Slider = binding.seekBar
        seekerBarController.init(seekerBar)
        duration = binding.duration
        mediaControllerAdapter.playbackState.observe(viewLifecycleOwner, seekerBarController)
        mediaControllerAdapter.metadata.observe(viewLifecycleOwner, seekerBarController)
        mediaControllerAdapter.repeatMode.observe(viewLifecycleOwner, seekerBarController)
        mediaControllerAdapter.playbackState.observe(viewLifecycleOwner, this)
        mediaControllerAdapter.metadata.observe(viewLifecycleOwner, this)
        return binding.root
    }

    override fun logTag(): String {
        return "PLY_TRKR_FRGMT"
    }

    override fun onChanged(data : Any) {
        if (data is MediaMetadataCompat) {
            val duration = data.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
            counter.duration = duration
            val durationString = formatTime(duration)
            updateDurationText(durationString)
        } else if (data is PlaybackStateCompat) {
            counter.updateState(data)
        }
    }

    private fun updateDurationText(duration: String) {
        this.duration!!.text = duration
    }
}