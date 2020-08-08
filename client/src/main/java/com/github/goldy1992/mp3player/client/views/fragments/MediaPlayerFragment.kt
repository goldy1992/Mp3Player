package com.github.goldy1992.mp3player.client.views.fragments

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.activities.MainActivity
import com.github.goldy1992.mp3player.client.databinding.FragmentMediaPlayerBinding
import com.github.goldy1992.mp3player.client.views.TrackViewPager
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerFragment : DestinationFragment(), LogTagger, Observer<MediaMetadataCompat> {

    @Inject
    lateinit var mediaControllerAdapter : MediaControllerAdapter

    @Inject
    lateinit var trackViewPager : TrackViewPager

    lateinit var titleToolbar : MaterialToolbar

    lateinit var appBarLayout: AppBarLayout

    @get:VisibleForTesting
    var trackToPlay: Uri? = null
        private set

    override fun lockDrawerLayout(): Boolean {
        return true
    }

     override fun setUpToolbar(toolbar : Toolbar) {
         titleToolbar.setOnClickListener {
             val navController = this.findNavController()
             navController.popBackStack()
         }

     }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMediaPlayerBinding.inflate(inflater)
        mediaControllerAdapter.queue.observe(viewLifecycleOwner, trackViewPager.queueObserver)
        mediaControllerAdapter.currentQueuePosition.observe(viewLifecycleOwner, trackViewPager.currentQueuePositionObserver)
        mediaControllerAdapter.metadata.observe(viewLifecycleOwner, trackViewPager.metadataObserver)
        mediaControllerAdapter.metadata.observe(viewLifecycleOwner, this)
        this.trackViewPager.init(binding.trackViewPager)
        this.titleToolbar = binding.titleToolbar
        this.appBarLayout = binding.appbarLayout
        setUpToolbar(titleToolbar)
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

        val artistText = t?.description?.extras?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
        titleToolbar.subtitle = artistText
    }
}