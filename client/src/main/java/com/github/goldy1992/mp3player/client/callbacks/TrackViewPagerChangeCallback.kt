package com.github.goldy1992.mp3player.client.callbacks

import android.support.v4.media.MediaMetadataCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.callbacks.metadata.MyMetadataCallback

class TrackViewPagerChangeCallback (
        /**  */
        private val mediaControllerAdapter: MediaControllerAdapter) : OnPageChangeCallback(),
        MetadataListener {
    /**  */
     var currentPosition: Int = -1

    /**
     *
     * @param position
     */
    override fun onPageSelected(position: Int) {
        if (currentPosition == position) {
            return
        }
        if (isSkipToNext(position)) {
            mediaControllerAdapter.skipToNext()
        } else if (isSkipToPrevious(position)) {
            mediaControllerAdapter.seekTo(0)
            mediaControllerAdapter.skipToPrevious()
        }
        currentPosition = position
    }

    private fun isSkipToNext(position: Int): Boolean {
        return position == currentPosition + 1
    }

    private fun isSkipToPrevious(position: Int): Boolean {
        return position == currentPosition - 1
    }

    /** Constructor  */
    init {
        mediaControllerAdapter.registerListener(this)
    }

    override fun onMetadataChanged(metadata: MediaMetadataCompat) {
        currentPosition = mediaControllerAdapter.getCurrentQueuePosition()
    }
}