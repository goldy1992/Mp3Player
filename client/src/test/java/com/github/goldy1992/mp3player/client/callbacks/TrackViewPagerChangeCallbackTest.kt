package com.github.goldy1992.mp3player.client.callbacks

import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TrackViewPagerChangeCallbackTest {
    private var trackViewPagerChangeCallback: TrackViewPagerChangeCallback? = null
    @Mock
    private val mediaControllerAdapter: MediaControllerAdapter? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(mediaControllerAdapter!!.currentQueuePosition).thenReturn(0)
        trackViewPagerChangeCallback = TrackViewPagerChangeCallback(mediaControllerAdapter)
    }

    @Test
    fun testSamePosition() {
        val initialPosition = 0
        trackViewPagerChangeCallback!!.currentPosition = initialPosition
        trackViewPagerChangeCallback!!.onPageSelected(initialPosition)
        Mockito.verify(mediaControllerAdapter, Mockito.never())!!.seekTo(ArgumentMatchers.anyLong())
        Mockito.verify(mediaControllerAdapter, Mockito.never())!!.skipToPrevious()
        Mockito.verify(mediaControllerAdapter, Mockito.never())!!.skipToNext()
        Assert.assertEquals(initialPosition.toLong(), trackViewPagerChangeCallback!!.currentPosition.toLong())
    }

    @Test
    fun testSkipToNext() {
        val initialPosition = 0
        trackViewPagerChangeCallback!!.currentPosition = initialPosition
        val skipToNextPosition = initialPosition + 1
        trackViewPagerChangeCallback!!.onPageSelected(skipToNextPosition)
        Assert.assertEquals(skipToNextPosition.toLong(), trackViewPagerChangeCallback!!.currentPosition.toLong())
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.skipToNext()
    }

    @Test
    fun testSkipToPrevious() {
        val initialPosition = 2
        trackViewPagerChangeCallback!!.currentPosition = initialPosition
        val skipToPreviousPosition = initialPosition - 1
        trackViewPagerChangeCallback!!.onPageSelected(skipToPreviousPosition)
        Assert.assertEquals(skipToPreviousPosition.toLong(), trackViewPagerChangeCallback!!.currentPosition.toLong())
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.seekTo(0)
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.skipToPrevious()
    }

    @Test
    fun testSkipMoreThanOnePosition() {
        val initialPosition = 2
        trackViewPagerChangeCallback!!.currentPosition = initialPosition
        val skipTwoPositions = initialPosition + 2
        trackViewPagerChangeCallback!!.onPageSelected(skipTwoPositions)
        Assert.assertEquals(skipTwoPositions.toLong(), trackViewPagerChangeCallback!!.currentPosition.toLong())
        Mockito.verify(mediaControllerAdapter, Mockito.never())!!.skipToPrevious()
        Mockito.verify(mediaControllerAdapter, Mockito.never())!!.skipToNext()
        Mockito.verify(mediaControllerAdapter, Mockito.never())!!.seekTo(ArgumentMatchers.anyLong())
    }
}