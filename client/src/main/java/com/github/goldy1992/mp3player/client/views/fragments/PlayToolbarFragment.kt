package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.databinding.FragmentPlaybackToolbarBinding
import com.github.goldy1992.mp3player.client.views.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.views.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.views.buttons.SkipToPreviousButton
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayToolbarFragment : Fragment(), LogTagger {

    @Inject
    lateinit var mediaControllerAdapter : MediaControllerAdapter

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
        val binding : FragmentPlaybackToolbarBinding = FragmentPlaybackToolbarBinding.inflate(layoutInflater)
        playPauseBtn.init(binding.playPauseButton)
        skipToPreviousBtn.init(binding.skipToPreviousButton)
        skipToNextBtn.init(binding.skipToNextButton)
        binding.playbackToolbar.setOnClickListener { goToMediaPlayerActivity() }
        return binding.root
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        mediaControllerAdapter.playbackState.observe(viewLifecycleOwner, playPauseBtn)
        view.setOnClickListener { goToMediaPlayerActivity() }
    }

    private fun goToMediaPlayerActivity() {
        if (findNavController().currentDestination?.id != R.id.media_player_fragment) {
            findNavController().navigate(R.id.go_to_media_player)
        }
    }

    override fun logTag(): String {
        return "PLY_TLBR_FRGMT"
    }
}