package com.github.goldy1992.mp3player.client.views.buttons

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.ImageView
import com.github.goldy1992.mp3player.commons.Constants
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepeatOneRepeatAllButtonTest : MediaButtonTestBase() {
    private var repeatOneRepeatAllButton: RepeatOneRepeatAllButton? = null
    @Before
    public override fun setup() {
        super.setup()
        repeatOneRepeatAllButton = RepeatOneRepeatAllButton(context, mediaControllerAdapter)
    }

    @Test
    fun testInit() {
        val imageView = mock<ImageView>()
        @PlaybackStateCompat.RepeatMode val expectedState = PlaybackStateCompat.REPEAT_MODE_NONE
        whenever(mediaControllerAdapter!!.repeatMode).thenReturn(expectedState)
        repeatOneRepeatAllButton!!.init(imageView)
        Assert.assertEquals(expectedState, repeatOneRepeatAllButton!!.state)
    }

    @Test
    fun testOnClickWhenRepeatNone() {
        runOnClick(PlaybackStateCompat.REPEAT_MODE_NONE, PlaybackStateCompat.REPEAT_MODE_ONE)
    }

    @Test
    fun testOnClickWhenRepeatOne() {
        runOnClick(PlaybackStateCompat.REPEAT_MODE_ONE, PlaybackStateCompat.REPEAT_MODE_ALL)
    }

    @Test
    fun testOnClickWhenRepeatAll() {
        runOnClick(PlaybackStateCompat.REPEAT_MODE_ALL, PlaybackStateCompat.REPEAT_MODE_NONE)
    }

    @Test
    fun testOnClickWhenRepeatInvalid() {
        runOnClick(PlaybackStateCompat.REPEAT_MODE_INVALID, PlaybackStateCompat.REPEAT_MODE_NONE)
    }

    @Test
    fun testOnPlaybackStateChangedWithNoRepeatMode() {
        val expectedState = mediaControllerAdapter!!.playbackState
        val state = PlaybackStateCompat.Builder().build()
        repeatOneRepeatAllButton!!.onPlaybackStateChanged(state)
        Assert.assertEquals(expectedState, repeatOneRepeatAllButton!!.state)
    }

    @Test
    fun testOnPlaybackStateChangedWithValidRepeatMode() {
        val expectedState = PlaybackStateCompat.REPEAT_MODE_ONE
        repeatOneRepeatAllButton!!.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)
        val extras = Bundle()
        extras.putInt(Constants.REPEAT_MODE, expectedState)
        val state = PlaybackStateCompat.Builder()
                .setExtras(extras)
                .build()
        whenever(mediaControllerAdapter!!.repeatMode).thenReturn(expectedState)
        repeatOneRepeatAllButton!!.onPlaybackStateChanged(state)
        Assert.assertEquals(expectedState, repeatOneRepeatAllButton!!.state)
    }

    private fun runOnClick(@PlaybackStateCompat.RepeatMode originalRepeatMode: Int,
                           @PlaybackStateCompat.RepeatMode expectedRepeatMode: Int) {
        val imageView = mock<ImageView>()
        repeatOneRepeatAllButton!!.setRepeatMode(originalRepeatMode)
        repeatOneRepeatAllButton!!.onClick(imageView)
        verify(mediaControllerAdapter, times(1))!!.repeatMode = expectedRepeatMode
    }
}