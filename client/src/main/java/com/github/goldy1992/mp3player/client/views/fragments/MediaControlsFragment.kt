package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.views.buttons.RepeatOneRepeatAllButton
import com.github.goldy1992.mp3player.client.views.buttons.ShuffleButton
import javax.inject.Inject

class MediaControlsFragment : MediaFragment() {

    @Inject
    lateinit var repeatOneRepeatAllButton: RepeatOneRepeatAllButton
    @Inject
    lateinit var shuffleButton: ShuffleButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_media_controls, container, true)
    }

    override fun logTag(): String {
        return "MDIA_CTRL_FGMT"
    }

    override fun mediaControllerListeners(): Set<Listener> {
        val toReturn : MutableSet<Listener> = HashSet()
        toReturn.add(repeatOneRepeatAllButton)
        toReturn.add(shuffleButton)
        return toReturn
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        repeatOneRepeatAllButton.init(view.findViewById(R.id.repeatOneRepeatAllButton))
        shuffleButton.init(view.findViewById(R.id.shuffleButton))
    }

    override fun initialiseDependencies() {
        createMediaFragmentSubcomponent()
            ?.inject(this)
    }

}