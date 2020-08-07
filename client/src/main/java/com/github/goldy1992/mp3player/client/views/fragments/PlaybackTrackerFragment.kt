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
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.SeekerBarController2
import com.github.goldy1992.mp3player.client.databinding.FragmentPlaybackTrackerBinding
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.client.views.SeekerBar
import com.github.goldy1992.mp3player.client.views.TimeCounter
import com.github.goldy1992.mp3player.commons.LogTagger
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
        mediaControllerAdapter.playbackState.observe(viewLifecycleOwner, this)
        mediaControllerAdapter.metadata.observe(viewLifecycleOwner, this)
        return binding.root
    }

    override fun logTag(): String {
        return "PLY_TRKR_FRGMT"
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        // init counter
        val counterView = view.findViewById<TextView>(R.id.timer)
        counter.init(counterView)
        // init seeker bar
        val seekerBar: SeekerBar = view.findViewById(R.id.seekBar)
        seekerBarController.init(seekerBar)
        // init duration
        duration = view.findViewById(R.id.duration)
    }

    override fun onChanged(data : Any) {
        if (data is MediaMetadataCompat) {
            val duration = data.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
            counter.duration = duration
            val durationString = formatTime(duration)
            updateDurationText(durationString)
            seekerBarController.onMetadataChanged(data)
        } else if (data is PlaybackStateCompat) {
            counter.updateState(data)
            seekerBarController.onPlaybackStateChanged(data)
        }
    }

    private fun updateDurationText(duration: String) {
        this.duration!!.text = duration
    }
}