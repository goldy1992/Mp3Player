package com.github.goldy1992.mp3player.client.views.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivity
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.views.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.views.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.views.buttons.SkipToPreviousButton
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_playback_toolbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlayToolbarFragment : MediaFragment() {

    @Inject
    lateinit var playPauseBtn: PlayPauseButton
    @Inject
    lateinit var skipToPreviousBtn: SkipToPreviousButton
    @Inject
    lateinit var skipToNextBtn: SkipToNextButton
    @Inject
    lateinit var componentClassMapper : ComponentClassMapper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_playback_toolbar, container, true)
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return setOf(playPauseBtn)
    }

    /**
     * @return A set of MediaBrowserConnectionListeners to be connected to.
     */
    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        return emptySet()
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        playPauseBtn.init(playPauseButton)
        skipToPreviousBtn.init(skipToPreviousButton)
        skipToNextBtn.init(skipToNextButton)

        if (!isMediaPlayerActivity) {
            playbackToolbar.setOnClickListener(View.OnClickListener { goToMediaPlayerActivity() })
        }
    }

    private val isMediaPlayerActivity: Boolean
        get() {
            val activity: Activity? = activity
            return activity != null && activity is MediaPlayerActivity
        }

    private fun goToMediaPlayerActivity() {

            findNavController().navigate(R.id.go_to_media_player)
//        val intent = Intent(context, componentClassMapper.mediaPlayerActivity)
//        startActivity(intent)
    }

    override fun logTag(): String {
        return "PLY_TLBR_FRGMT"
    }
}