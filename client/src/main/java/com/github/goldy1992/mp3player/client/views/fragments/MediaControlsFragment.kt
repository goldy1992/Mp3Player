package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.databinding.FragmentMediaControlsBinding
import com.github.goldy1992.mp3player.client.views.buttons.RepeatOneRepeatAllButton
import com.github.goldy1992.mp3player.client.views.buttons.ShuffleButton
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint(Fragment::class)
class MediaControlsFragment : Hilt_MediaControlsFragment(), LogTagger {

    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    @Inject
    lateinit var repeatOneRepeatAllButton: RepeatOneRepeatAllButton
    @Inject
    lateinit var shuffleButton: ShuffleButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding =  FragmentMediaControlsBinding.inflate(inflater)
        this.repeatOneRepeatAllButton.init(binding.repeatOneRepeatAllButton)
        this.shuffleButton.init(binding.shuffleButton)
        mediaControllerAdapter.repeatMode.observe(viewLifecycleOwner, repeatOneRepeatAllButton)
        mediaControllerAdapter.shuffleMode.observe(viewLifecycleOwner, shuffleButton)
        return binding.root
    }

    override fun logTag(): String {
        return "MDIA_CTRL_FGMT"
    }
}