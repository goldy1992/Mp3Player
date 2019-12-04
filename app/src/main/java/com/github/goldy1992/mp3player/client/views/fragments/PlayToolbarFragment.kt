package com.github.goldy1992.mp3player.client.views.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivity
import com.github.goldy1992.mp3player.client.utils.IntentUtils.createGoToMediaPlayerActivity
import com.github.goldy1992.mp3player.client.views.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.views.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.views.buttons.SkipToPreviousButton
import kotlinx.android.synthetic.main.fragment_playback_toolbar.*
import javax.inject.Inject


class PlayToolbarFragment : Fragment() {

    @Inject
    lateinit var playPauseBtn: PlayPauseButton
    @Inject
    lateinit var skipToPreviousBtn: SkipToPreviousButton
    @Inject
    lateinit var skipToNextBtn: SkipToNextButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        initialiseDependencies()
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_playback_toolbar, container, true)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        playPauseBtn?.init(playPauseButton)
        skipToPreviousBtn?.init(skipToPreviousButton)
        skipToNextBtn?.init(skipToNextButton)

        if (!isMediaPlayerActivity) {
            playbackToolbar.setOnClickListener(View.OnClickListener { v: View? -> goToMediaPlayerActivity() })
        }
    }

    private val isMediaPlayerActivity: Boolean
        private get() {
            val activity: Activity? = activity
            return activity != null && activity is MediaPlayerActivity
        }

    private fun goToMediaPlayerActivity() {
        val intent = createGoToMediaPlayerActivity(context!!)
        startActivity(intent)
    }

    fun initialiseDependencies() {
        val activity = activity
        if (null != activity && activity is MediaActivityCompat) {
            val mediaPlayerActivity = getActivity() as MediaActivityCompat?
            mediaPlayerActivity!!.mediaActivityCompatComponent!!.playbackButtonsSubcomponent()
                    .inject(this)
        }
    }

    companion object {
        private const val LOG_TAG = "PLY_PAUSE_BTN"
    }
}