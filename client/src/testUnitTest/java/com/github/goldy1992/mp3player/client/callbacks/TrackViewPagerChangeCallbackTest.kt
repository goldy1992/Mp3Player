package com.github.goldy1992.mp3player.client.callbacks

import com.github.goldy1992.mp3player.client.MediaControllerAdapter
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
class TrackViewPagerChangeCallbackTest {
    private var trackViewPagerChangeCallback: TrackViewPagerChangeCallback? = null

    private val mediaControllerAdapter: MediaControllerAdapter = mock<MediaControllerAdapter>()

    @Before
    fun setup() {
        whenever(mediaControllerAdapter.getCurrentQueuePosition()).thenReturn(0)
        trackViewPagerChangeCallback = TrackViewPagerChangeCallback(mediaControllerAdapter)
    }

    @Test
    fun testSamePosition() {
        val initialPosition = 0
        trackViewPagerChangeCallback!!.currentPosition = initialPosition
        trackViewPagerChangeCallback!!.onPageSelected(initialPosition)
        verify(mediaControllerAdapter, never()).seekTo(any<Long>())
        verify(mediaControllerAdapter, never()).skipToPrevious()
        verify(mediaControllerAdapter, never()).skipToNext()
        Assert.assertEquals(initialPosition.toLong(), trackViewPagerChangeCallback!!.currentPosition.toLong())
    }

    @Test
    fun testSkipToNext() {
        val initialPosition = 0
        trackViewPagerChangeCallback!!.currentPosition = initialPosition
        val skipToNextPosition = initialPosition + 1
        trackViewPagerChangeCallback!!.onPageSelected(skipToNextPosition)
        Assert.assertEquals(skipToNextPosition.toLong(), trackViewPagerChangeCallback!!.currentPosition.toLong())
        verify(mediaControllerAdapter, times(1)).skipToNext()
    }

    @Test
    fun testSkipToPrevious() {
        val initialPosition = 2
        trackViewPagerChangeCallback!!.currentPosition = initialPosition
        val skipToPreviousPosition = initialPosition - 1
        trackViewPagerChangeCallback!!.onPageSelected(skipToPreviousPosition)
        Assert.assertEquals(skipToPreviousPosition.toLong(), trackViewPagerChangeCallback!!.currentPosition.toLong())
        verify(mediaControllerAdapter, times(1)).seekTo(0)
        verify(mediaControllerAdapter, times(1)).skipToPrevious()
    }

    @Test
    fun testSkipMoreThanOnePosition() {
        val initialPosition = 2
        trackViewPagerChangeCallback!!.currentPosition = initialPosition
        val skipTwoPositions = initialPosition + 2
        trackViewPagerChangeCallback!!.onPageSelected(skipTwoPositions)
        Assert.assertEquals(skipTwoPositions.toLong(), trackViewPagerChangeCallback!!.currentPosition.toLong())
        verify(mediaControllerAdapter, never()).skipToPrevious()
        verify(mediaControllerAdapter, never()).skipToNext()
        verify(mediaControllerAdapter, never()).seekTo(any<Long>())
    }
}