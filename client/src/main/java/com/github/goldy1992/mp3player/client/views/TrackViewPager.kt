package com.github.goldy1992.mp3player.client.views

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.views.adapters.TrackViewAdapter
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject

class TrackViewPager

    @Inject
    constructor(val trackViewAdapter: TrackViewAdapter,
                private val mediaControllerAdapter: MediaControllerAdapter)
    : LogTagger {

    lateinit var view: ViewPager2

    private val pageChangeCallback : PageChangeCallback = PageChangeCallback()

    val queueObserver = QueueObserver()

    val metadataObserver = MetadataObserver()

    val currentQueuePositionObserver = CurrentQueuePositionObserver()

    /**  */
    var currentPosition: Int = -1

    fun init(viewPager2: ViewPager2) {
        this.view = viewPager2
        view.adapter = trackViewAdapter
        view.registerOnPageChangeCallback(pageChangeCallback)
    }



    private fun setCurrentItem() {
        view.setCurrentItem(mediaControllerAdapter.getCurrentQueuePosition(), false)
    }

    override fun logTag(): String {
      return "TRACK_VIEW_PAGER"
    }

    inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {
        /**
         *
         * @param position
         */
        override fun onPageSelected(position: Int) {

            if (position < 0 || currentPosition == position) {
                return
            }
            if (isSkipToNext(position)) {
                mediaControllerAdapter.skipToNext()
            } else if (isSkipToPrevious(position)) {
                mediaControllerAdapter.seekTo(0)
                mediaControllerAdapter.skipToPrevious()
            }
        }

        private fun isSkipToNext(position: Int): Boolean {
            return position == (currentPosition + 1)
        }

        private fun isSkipToPrevious(position: Int): Boolean {
            return position == (currentPosition - 1)
        }


    }

    inner class QueueObserver : Observer<List<MediaSessionCompat.QueueItem>> {
        override fun onChanged(t: List<MediaSessionCompat.QueueItem>?) {
            setCurrentItem()
            trackViewAdapter.onQueueChanged(t)
        }
    }

    inner class MetadataObserver : Observer<MediaMetadataCompat> {
        override fun onChanged(t: MediaMetadataCompat) {
            val id = t.description?.mediaId

            if (id != null) {
                setCurrentItem()
            }
        }
    }

    inner class CurrentQueuePositionObserver : Observer<Int> {
        override fun onChanged(t: Int) {
            currentPosition = t
            setCurrentItem()
        }

    }
}