package com.github.goldy1992.mp3player.client.views

import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.TextView
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TimeCounterTest {

    private val view: TextView = mock<TextView>()
    private val mediaControllerAdapter: MediaControllerAdapter = mock<MediaControllerAdapter>()
    private lateinit var handler: Handler
    private lateinit var timeCounter: TimeCounter

    val POSITION = 3424L
    val DURATION = 100000L
    @Before

    fun setup() {
        handler = Handler(Looper.getMainLooper())
        timeCounter = TimeCounter(mediaControllerAdapter)
        timeCounter.init(view)
    }

    @Test
    fun testNotInitialised() {
        val expectedPosition = 0L
        val expectedState = PlaybackStateCompat.STATE_PLAYING
        timeCounter.init(null)
        timeCounter.updateState(createState(expectedState, 5L))
        Assert.assertEquals(expectedState, timeCounter.currentState)
        // current position will not be updated will not updated as text view is null
        Assert.assertEquals(expectedPosition, timeCounter.currentPosition)
    }

    @Test
    fun updateStateResetTimerTest() {
        val state = createState(PlaybackStateCompat.STATE_STOPPED, POSITION)
        timeCounter.updateState(state)
        Assert.assertFalse("TimerCounter should not be running", timeCounter.isRunning)
        Assert.assertEquals("currentTime should be reset to 0", 0L, timeCounter.currentPosition)
    }

    @Test
    fun updateStateHaltTimerTest() {
        setStatePlaying()
        val spiedTimer = spy(timeCounter.timer)
        timeCounter.timer = spiedTimer
        val state = createState(PlaybackStateCompat.STATE_PAUSED, POSITION)
        timeCounter.updateState(state)
       // verify(spiedTimer)!!.shutdown()
        Assert.assertFalse("TimerCounter should not be running", timeCounter.isRunning)
        Assert.assertEquals("currentTime should be equal to the position parameter", POSITION, timeCounter.currentPosition)
    }

    @Test
    fun updateStateWorkTest() {
        timeCounter.duration = DURATION
        val state = createState(PlaybackStateCompat.STATE_PLAYING, POSITION)
        timeCounter.updateState(state)
        Assert.assertTrue("TimerCounter should be running", timeCounter.isRunning)
    }

    @Test
    fun testSeekTo() {
        val position = 28L
        val expectedText = formatTime(position)
        timeCounter.seekTo(position)
        verify(view, times(1)).text = expectedText
        Assert.assertEquals(position, timeCounter.currentPosition)
    }

    private fun createState(state: Int, position: Long): PlaybackStateCompat {
        return PlaybackStateCompat.Builder().setState(state, position, 0f, 0L).build()
    }

    private fun setStatePlaying() {
        val state = createState(PlaybackStateCompat.STATE_PLAYING, POSITION)
        timeCounter.updateState(state)
    }
}