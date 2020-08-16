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

    fun init(viewPager2: ViewPager2) {
        this.view = viewPager2
        view.adapter = trackViewAdapter
        view.registerOnPageChangeCallback(pageChangeCallback)
    }



    private fun setCurrentItem() {
        view.setCurrentItem(mediaControllerAdapter.calculateCurrentQueuePosition(), false)
    }

    override fun logTag(): String {
      return "TRACK_VIEW_PAGER"
    }

    inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {
        /**
         *
         * @param newPosition
         */
        override fun onPageSelected(newPosition: Int) {

            val currentQueuePosition = mediaControllerAdapter.calculateCurrentQueuePosition()

            if (newPosition < 0 || currentQueuePosition == newPosition) {
                return
            }
            if (isSkipToNext(newPosition, currentQueuePosition)) {
                mediaControllerAdapter.skipToNext()
            } else if (isSkipToPrevious(newPosition, currentQueuePosition)) {
                mediaControllerAdapter.seekTo(0)
                mediaControllerAdapter.skipToPrevious()
            }
        }

        private fun isSkipToNext(newPosition: Int, currentPosition : Int): Boolean {
            return newPosition == (currentPosition + 1)
        }

        private fun isSkipToPrevious(newPosition: Int, currentPosition : Int): Boolean {
            return newPosition == (currentPosition - 1)
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

}