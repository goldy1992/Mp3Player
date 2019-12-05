package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import com.github.goldy1992.mp3player.commons.Constants
import kotlinx.android.synthetic.main.fragment_playback_speed_controls.*
import javax.inject.Inject

class PlaybackSpeedControlsFragment : AsyncFragment(), PlaybackStateListener {

    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    private var speed = 1.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        initialiseDependencies()
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_playback_speed_controls, container, true)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        decreasePlaybackSpeedButton.setOnClickListener(View.OnClickListener { v: View? -> decreasePlaybackSpeed() })

        increasePlaybackSpeedButton.setOnClickListener(View.OnClickListener { v: View? -> increasePlaybackSpeed() })

        // register listeners
        mediaControllerAdapter!!.registerPlaybackStateListener(this)
        //update GUI
        onPlaybackStateChanged(mediaControllerAdapter!!.playbackStateCompat!!)
    }

    private fun updatePlaybackSpeedText(speed: Float) {
        val r = Runnable { playbackSpeedTextView!!.text = getString(R.string.PLAYBACK_SPEED_VALUE, speed) }
        mainUpdater.post(r)
    }

    @VisibleForTesting
    fun increasePlaybackSpeed() {
        worker!!.post {
            val extras = Bundle()
            mediaControllerAdapter!!.sendCustomAction(Constants.INCREASE_PLAYBACK_SPEED, extras)
        }
    }

    @VisibleForTesting
    fun decreasePlaybackSpeed() {
        worker!!.post {
            val extras = Bundle()
            mediaControllerAdapter!!.sendCustomAction(Constants.DECREASE_PLAYBACK_SPEED, extras)
        }
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        speed = state.playbackSpeed
        if (speed > 0) {
            updatePlaybackSpeedText(speed)
        }
    }

    fun initialiseDependencies() {
        val component = (activity as MediaActivityCompat?)!!.mediaActivityCompatComponent
        component!!.inject(this)
    }

}