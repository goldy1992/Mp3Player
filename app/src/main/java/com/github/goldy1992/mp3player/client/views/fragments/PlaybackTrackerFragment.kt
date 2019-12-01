package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.SeekerBarController2
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.client.views.SeekerBar
import com.github.goldy1992.mp3player.client.views.TimeCounter
import javax.inject.Inject

class PlaybackTrackerFragment : AsyncFragment(), PlaybackStateListener, MetadataListener {
    private var mediaControllerAdapter: MediaControllerAdapter? = null
    private var duration: TextView? = null
    private var seekerBarController: SeekerBarController2? = null
    private var counter: TimeCounter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        initialiseDependencies()
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_playback_tracker, container, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        // init counter
        val counterView = view.findViewById<TextView>(R.id.timer)
        counter!!.init(counterView)
        // init seeker bar
        val seekerBar: SeekerBar = view.findViewById(R.id.seekBar)
        seekerBarController!!.init(seekerBar)
        // init duration
        duration = view.findViewById(R.id.duration)
        // init MediaController listeners
        registerMediaControllerListeners()
        // update GUI state
        onMetadataChanged(mediaControllerAdapter!!.metadata)
        onPlaybackStateChanged(mediaControllerAdapter!!.playbackStateCompat)
    }

    private fun registerMediaControllerListeners() {
        mediaControllerAdapter!!.registerPlaybackStateListener(this)
        mediaControllerAdapter!!.registerMetaDataListener(this)
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        counter!!.updateState(state)
        seekerBarController!!.onPlaybackStateChanged(state)
    }

    override fun onMetadataChanged(metadata: MediaMetadataCompat) {
        val duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
        counter!!.setDuration(duration)
        val durationString = formatTime(duration)
        mainUpdater.post { updateDurationText(durationString) }
        seekerBarController!!.onMetadataChanged(metadata)
    }

    fun initialiseDependencies() {
        val component = (activity as MediaActivityCompat?)!!.mediaActivityCompatComponent
                .playbackTrackerSubcomponent()
        component.inject(this)
    }

    private fun updateDurationText(duration: String) {
        this.duration!!.text = duration
    }

    @Inject
    fun setTimeCounter(timeCounter: TimeCounter?) {
        counter = timeCounter
    }

    @Inject
    fun setMediaControllerAdapter(mediaControllerAdapter: MediaControllerAdapter?) {
        this.mediaControllerAdapter = mediaControllerAdapter
    }

    @Inject
    fun setSeekerBarController(controller: SeekerBarController2?) {
        seekerBarController = controller
    }
}