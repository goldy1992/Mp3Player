package com.github.goldy1992.mp3player.client.callbacks

import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import javax.inject.Inject

class TrackViewPagerChangeCallback

    @Inject
    constructor(private val mediaControllerAdapter: MediaControllerAdapter)

    : OnPageChangeCallback(), Observer<Int> {

    /**  */
     var currentPosition: Int = -1

    /**
     *
     * @param position
     */
    override fun onPageSelected(position: Int) {
        val isUninitialised = currentPosition < 0
        if (isUninitialised) {
            currentPosition = position
            return
        }
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

    override fun onChanged(t: Int?) {
        currentPosition = t!!
    }
}