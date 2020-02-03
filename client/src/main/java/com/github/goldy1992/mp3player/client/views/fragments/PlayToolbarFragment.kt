package com.github.goldy1992.mp3player.client.views.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivity
//import com.github.goldy1992.mp3player.client.utils.IntentUtils.createGoToMediaPlayerActivity
import com.github.goldy1992.mp3player.client.views.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.views.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.views.buttons.SkipToPreviousButton
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.android.synthetic.main.fragment_playback_toolbar.*
import javax.inject.Inject


class PlayToolbarFragment : Fragment(), LogTagger {

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
        initialiseDependencies()
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_playback_toolbar, container, true)
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
        val intent = Intent(context, componentClassMapper.mediaPlayerActivity)

        startActivity(intent)
    }

    fun initialiseDependencies() {
        val activity = activity
        if (null != activity && activity is MediaActivityCompat) {
            val mediaPlayerActivity = getActivity() as MediaActivityCompat?
            mediaPlayerActivity!!.mediaActivityCompatComponent.playbackButtonsSubcomponent()
                    .inject(this)
        }
    }

    override fun logTag(): String {
        return "PLY_PAUSE_BTN"
    }
}