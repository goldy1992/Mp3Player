package com.github.goldy1992.mp3player.client.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.views.adapters.TrackViewAdapter
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TrackViewPagerTest {

    private val mediaControllerAdapter: MediaControllerAdapter = mock<MediaControllerAdapter>()

    private val trackViewAdapter : TrackViewAdapter = mock<TrackViewAdapter>()

    private val trackViewPager : TrackViewPager = TrackViewPager(trackViewAdapter, mediaControllerAdapter)

    lateinit var callback : TrackViewPager.PageChangeCallback

    @Before
    fun setup() {
           init()
    }



    @Test
    fun testSamePosition() {
        val initialPosition = 0
        whenever(mediaControllerAdapter.calculateCurrentQueuePosition()).thenReturn(initialPosition)
        callback.onPageSelected(initialPosition)
        verify(mediaControllerAdapter, never()).seekTo(any<Long>())
        verify(mediaControllerAdapter, never()).skipToPrevious()
        verify(mediaControllerAdapter, never()).skipToNext()
       }

    @Test
    fun testSkipToNext() {
        val initialPosition = 0
        whenever(mediaControllerAdapter.calculateCurrentQueuePosition()).thenReturn(initialPosition)
        val skipToNextPosition = initialPosition + 1
        callback.onPageSelected(skipToNextPosition)
        verify(mediaControllerAdapter, times(1)).skipToNext()
    }

    @Test
    fun testSkipToPrevious() {
        val initialPosition = 2
        whenever(mediaControllerAdapter.calculateCurrentQueuePosition()).thenReturn(initialPosition)
        val skipToPreviousPosition = initialPosition - 1
        callback.onPageSelected(skipToPreviousPosition)
        verify(mediaControllerAdapter, times(1)).seekTo(0)
        verify(mediaControllerAdapter, times(1)).skipToPrevious()
    }

    @Test
    fun testSkipMoreThanOnePosition() {
        val initialPosition = 2
        whenever(mediaControllerAdapter.calculateCurrentQueuePosition()).thenReturn(0)
        val skipTwoPositions = initialPosition + 2
        callback.onPageSelected(skipTwoPositions)
        verify(mediaControllerAdapter, never()).skipToPrevious()
        verify(mediaControllerAdapter, never()).skipToNext()
        verify(mediaControllerAdapter, never()).seekTo(any<Long>())
    }

    private fun init() {
        val viewPager2: ViewPager2 = mock<ViewPager2>()
        whenever(viewPager2.registerOnPageChangeCallback(any())).thenAnswer {
            val expectedCallback = it.arguments[0]

            if (expectedCallback is TrackViewPager.PageChangeCallback) {
                callback = expectedCallback
            }
        }
        trackViewPager.init(viewPager2)
    }
}