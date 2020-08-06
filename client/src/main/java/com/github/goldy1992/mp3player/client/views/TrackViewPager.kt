package com.github.goldy1992.mp3player.client.views

import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.TrackViewPagerChangeCallback
import com.github.goldy1992.mp3player.client.views.adapters.TrackViewAdapter
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject

class TrackViewPager

    @Inject
    constructor(val trackViewAdapter: TrackViewAdapter,
                val trackViewPagerChangeCallback: TrackViewPagerChangeCallback,
                private val mediaControllerAdapter: MediaControllerAdapter)
    :  Observer<MediaMetadataCompat>, LogTagger {

    lateinit var view: ViewPager2

    fun init(viewPager2: ViewPager2) {
        this.view = viewPager2
        view.adapter = trackViewAdapter
        view.registerOnPageChangeCallback(trackViewPagerChangeCallback)
    }



    private fun setCurrentItem() {
        view.setCurrentItem(mediaControllerAdapter.getCurrentQueuePosition(), false)
    }

    override fun logTag(): String {
      return "TRACK_VIEW_PAGER"
    }

    /**
     * Called when the data is changed.
     * @param t  The new data
     */
    override fun onChanged(t: MediaMetadataCompat?) {
        val id = t?.description?.mediaId

        if (id != null) {
            setCurrentItem()
        }
    }

}