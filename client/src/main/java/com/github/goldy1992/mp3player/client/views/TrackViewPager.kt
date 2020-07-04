package com.github.goldy1992.mp3player.client.views

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.viewpager2.widget.ViewPager2
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.TrackViewPagerChangeCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.callbacks.queue.QueueListener
import com.github.goldy1992.mp3player.client.views.adapters.TrackViewAdapter
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject

class TrackViewPager

    @Inject
    constructor(private val trackViewAdapter: TrackViewAdapter,
                private val trackViewPagerChangeCallback: TrackViewPagerChangeCallback,
                private val mediaControllerAdapter: MediaControllerAdapter)
    : MediaBrowserConnectionListener, MetadataListener, QueueListener, LogTagger {

    lateinit var view: ViewPager2

    fun init(viewPager2: ViewPager2) {
        this.view = viewPager2
    }

    override fun onConnected() {
        trackViewAdapter.onQueueChanged(mediaControllerAdapter.getQueue()!!)
        view.adapter = trackViewAdapter
        view.registerOnPageChangeCallback(trackViewPagerChangeCallback)
        setCurrentItem()
    }

    override fun onMetadataChanged(metadata: MediaMetadataCompat) {
       trackViewPagerChangeCallback.onMetadataChanged(metadata)
    }

    override fun onQueueChanged(queue: List<MediaSessionCompat.QueueItem>) {
        trackViewAdapter.onQueueChanged(queue)
        setCurrentItem()
    }

    private fun setCurrentItem() {
        view.setCurrentItem(mediaControllerAdapter.getCurrentQueuePosition(), false)
    }

    override fun logTag(): String {
      return "TRACK_VIEW_PAGER"
    }

}