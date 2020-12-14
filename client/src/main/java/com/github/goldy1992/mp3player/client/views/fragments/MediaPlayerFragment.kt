package com.github.goldy1992.mp3player.client.views.fragments

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.databinding.FragmentMediaPlayerBinding
import com.github.goldy1992.mp3player.client.views.TrackViewPager
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint(DestinationFragment::class)
class MediaPlayerFragment : Hilt_MediaPlayerFragment(), LogTagger, Observer<MediaMetadataCompat> {

    @Inject
    lateinit var mediaControllerAdapter : MediaControllerAdapter

    @Inject
    lateinit var trackViewPager : TrackViewPager

    lateinit var titleToolbar : MaterialToolbar

    lateinit var appBarLayout: AppBarLayout



    override fun lockDrawerLayout(): Boolean {
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMediaPlayerBinding.inflate(inflater)
        mediaControllerAdapter.queue.observe(viewLifecycleOwner, trackViewPager.queueObserver)
        mediaControllerAdapter.metadata.observe(viewLifecycleOwner, trackViewPager.metadataObserver)
        mediaControllerAdapter.metadata.observe(viewLifecycleOwner, this)
        this.trackViewPager.init(binding.trackViewPager)
        this.titleToolbar = binding.titleToolbar
        this.appBarLayout = binding.appbarLayout

        titleToolbar.setOnClickListener {
            val navController = this.findNavController()
            navController.popBackStack()
        }
        return binding.root
    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "MEDIA_PLAYER_FRAGMENT"
    }

    /**
     * Called when the data is changed.
     * @param t  The new data
     */
    override fun onChanged(t: MediaMetadataCompat?) {
        val titleText = t?.description?.title.toString()
        titleToolbar.title = titleText
        val artistText = t?.description?.subtitle
        titleToolbar.subtitle = artistText
    }
}