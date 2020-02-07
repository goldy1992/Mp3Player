package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.views.buttons.RepeatOneRepeatAllButton
import com.github.goldy1992.mp3player.client.views.buttons.ShuffleButton
import javax.inject.Inject

class MediaControlsFragment : Fragment() {
    private var repeatOneRepeatAllButton: RepeatOneRepeatAllButton? = null
    private var shuffleButton: ShuffleButton? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        initialiseDependencies()
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_media_controls, container, true)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        repeatOneRepeatAllButton!!.init(view.findViewById(R.id.repeatOneRepeatAllButton))
        shuffleButton!!.init(view.findViewById(R.id.shuffleButton))
    }

    fun initialiseDependencies() {
        val component = (activity as MediaActivityCompat?)!!.mediaActivityCompatComponent
        component.playbackButtonsSubcomponent()
                .inject(this)
    }

    @Inject
    fun setRepeatOneRepeatAllButton(repeatOneRepeatAllButton: RepeatOneRepeatAllButton?) {
        this.repeatOneRepeatAllButton = repeatOneRepeatAllButton
    }

    @Inject
    fun setShuffleButton(shuffleButton: ShuffleButton?) {
        this.shuffleButton = shuffleButton
    }

    companion object {
        private const val LOG_TAG = "PLY_PAUSE_BTN"
    }
}