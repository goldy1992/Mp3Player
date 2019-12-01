package com.github.goldy1992.mp3player.client.views.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivity
import com.github.goldy1992.mp3player.client.utils.IntentUtils.createGoToMediaPlayerActivity
import com.github.goldy1992.mp3player.client.views.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.views.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.views.buttons.SkipToPreviousButton
import javax.inject.Inject

class PlayToolBarFragment : Fragment() {
    private var playPauseButton: PlayPauseButton? = null
    private var skipToPreviousButton: SkipToPreviousButton? = null
    private var skipToNextButton: SkipToNextButton? = null
    protected var toolbar: Toolbar? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        initialiseDependencies()
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_playback_toolbar, container, true)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        toolbar = view.findViewById(R.id.playbackToolbar)
        skipToPreviousButton!!.init(view.findViewById(R.id.skip_to_previous))
        skipToNextButton!!.init(view.findViewById(R.id.skip_to_next))
        playPauseButton!!.init(view.findViewById(R.id.playPauseButton))
        if (!isMediaPlayerActivity) {
            toolbar.setOnClickListener(View.OnClickListener { v: View? -> goToMediaPlayerActivity() })
        }
    }

    @Inject
    fun setPlayPauseButton(playPauseButton: PlayPauseButton?) {
        this.playPauseButton = playPauseButton
    }

    @Inject
    fun setSkipToPreviousButton(skipToPreviousButton: SkipToPreviousButton?) {
        this.skipToPreviousButton = skipToPreviousButton
    }

    @Inject
    fun setSkipToNextButton(skipToNextButton: SkipToNextButton?) {
        this.skipToNextButton = skipToNextButton
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