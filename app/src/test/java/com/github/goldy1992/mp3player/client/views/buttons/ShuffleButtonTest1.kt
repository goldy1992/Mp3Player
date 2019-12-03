package com.github.goldy1992.mp3player.client.views.buttons

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ShuffleMode
import android.view.View
import com.github.goldy1992.mp3player.commons.Constants
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ShuffleButtonTest : MediaButtonTestBase() {
    private var shuffleButton: ShuffleButton? = null
    @Before
    public override fun setup() {
        super.setup()
        shuffleButton = ShuffleButton(context!!, mediaControllerAdapter!!, handler!!)
    }

    @Test
    fun updateStateShuffleModeAll() {
        @ShuffleMode val shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_ALL
        shuffleButton!!.updateState(shuffleMode)
        Assert.assertEquals(shuffleMode.toLong(), shuffleButton!!.shuffleMode)
    }

    @Test
    fun updateStateNotShuffleModeAll() {
        @ShuffleMode val shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE
        shuffleButton!!.updateState(shuffleMode)
        Assert.assertEquals(shuffleMode.toLong(), shuffleButton!!.shuffleMode)
    }

    @Test
    fun testOnClickChangeToShuffleAll() {
        @ShuffleMode val currentShuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE
        @ShuffleMode val expectedShuffleMode = PlaybackStateCompat.SHUFFLE_MODE_ALL
        shuffleButton!!.shuffleMode = currentShuffleMode
        shuffleButton!!.onClick(Mockito.mock(View::class.java))
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.shuffleMode = expectedShuffleMode
    }

    @Test
    fun testOnClickChangeToShuffleNone() {
        @ShuffleMode val currentShuffleMode = PlaybackStateCompat.SHUFFLE_MODE_ALL
        @ShuffleMode val expectedShuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE
        shuffleButton!!.shuffleMode = currentShuffleMode
        shuffleButton!!.onClick(Mockito.mock(View::class.java))
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.shuffleMode = expectedShuffleMode
    }

    @Test
    fun testOnPlaybackStateChanged() {
        @ShuffleMode val expectedShuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE
        val extras = Bundle()
        extras.putInt(Constants.SHUFFLE_MODE, expectedShuffleMode)
        val playbackState = PlaybackStateCompat.Builder().setExtras(extras).build()
        shuffleButton!!.onPlaybackStateChanged(playbackState)
        Assert.assertEquals(expectedShuffleMode.toLong(), shuffleButton!!.shuffleMode)
    }
}