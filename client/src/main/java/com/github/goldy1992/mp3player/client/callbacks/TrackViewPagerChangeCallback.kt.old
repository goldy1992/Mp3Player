package com.github.goldy1992.mp3player.client.callbacks

import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.github.goldy1992.mp3player.client.MediaControllerAdapter

class TrackViewPagerChangeCallback(
        /**  */
        private val mediaControllerAdapter: MediaControllerAdapter) : OnPageChangeCallback() {
    /**  */
    var currentPosition: Int

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
        currentPosition = mediaControllerAdapter.currentQueuePosition
    }
}