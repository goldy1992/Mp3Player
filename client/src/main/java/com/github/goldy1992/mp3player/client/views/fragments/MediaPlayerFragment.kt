package com.github.goldy1992.mp3player.client.views.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.callbacks.Listener
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
        this.trackViewPager.init(binding.trackViewPager)
        return binding.root
    }
    override fun mediaControllerListeners(): Set<Listener> {
        return setOf(trackViewPager)
    }



    /**
     * @return A set of MediaBrowserConnectionListeners to be connected to.
     */
    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        return setOf(this, trackViewPager)
    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "MEDIA_PLAYER_FRAGMENT"
    }
}