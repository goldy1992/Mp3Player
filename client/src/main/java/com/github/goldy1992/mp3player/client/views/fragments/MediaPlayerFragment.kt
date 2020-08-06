package com.github.goldy1992.mp3player.client.views.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.databinding.FragmentMediaPlayerBinding
import com.github.goldy1992.mp3player.client.views.TrackViewPager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerFragment : MediaFragment() {

    @Inject
    lateinit var trackViewPager : TrackViewPager

    @get:VisibleForTesting
    var trackToPlay: Uri? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMediaPlayerBinding.inflate(layoutInflater)
        mediaControllerAdapter.queue.observe(viewLifecycleOwner, trackViewPager.trackViewAdapter)
        mediaControllerAdapter.currentQueuePosition.observe(viewLifecycleOwner, trackViewPager.trackViewPagerChangeCallback)
        this.trackViewPager.init(binding.trackViewPager)
        return binding.root
    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "MEDIA_PLAYER_FRAGMENT"
    }
}