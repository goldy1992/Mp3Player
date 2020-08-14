package com.github.goldy1992.mp3player.client.views.buttons

import android.support.v4.media.session.PlaybackStateCompat
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepeatOneRepeatAllButtonTest : MediaButtonTestBase() {

    private lateinit var repeatOneRepeatAllButton: RepeatOneRepeatAllButton

    @Before
    public override fun setup() {
        super.setup()
        repeatOneRepeatAllButton = RepeatOneRepeatAllButton(context, mediaControllerAdapter)
    }

    @Test
    fun testInit() {

        @PlaybackStateCompat.RepeatMode val expectedState = PlaybackStateCompat.REPEAT_MODE_NONE
        Assert.assertEquals(expectedState, repeatOneRepeatAllButton.state)
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
        val expectedState = PlaybackStateCompat.REPEAT_MODE_NONE
        repeatOneRepeatAllButton.onChanged(expectedState)
        Assert.assertEquals(expectedState, repeatOneRepeatAllButton.state)
    }

    @Test
    fun testOnPlaybackStateChangedWithValidRepeatMode() {
        repeatOneRepeatAllButton.init(view)
        val expectedState = PlaybackStateCompat.REPEAT_MODE_ONE
        repeatOneRepeatAllButton.onChanged(expectedState)
        Assert.assertEquals(expectedState, repeatOneRepeatAllButton.state)
    }

    private fun runOnClick(@PlaybackStateCompat.RepeatMode originalRepeatMode: Int,
                           @PlaybackStateCompat.RepeatMode expectedRepeatMode: Int) {
        repeatOneRepeatAllButton.setRepeatMode(originalRepeatMode)
        repeatOneRepeatAllButton.onClick(view)
        verify(mediaControllerAdapter, times(1)).setRepeatMode(expectedRepeatMode)
    }
}