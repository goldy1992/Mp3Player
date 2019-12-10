package com.github.goldy1992.mp3player.client

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.views.SeekerBar
import com.github.goldy1992.mp3player.client.views.TimeCounter
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowSeekBar
import org.robolectric.shadows.ShadowValueAnimator

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [26], shadows = [ShadowValueAnimator::class, ShadowSeekBar::class])
class SeekerBarController2Test {
    private var m_context: Context? = null

    private val m_mediaControllerAdapter: MediaControllerAdapter = mock<MediaControllerAdapter>()
    private lateinit var timeCounter: TimeCounter
    private lateinit var m_seekerBarController2: SeekerBarController2
    private lateinit var m_seekerBar: SeekerBar
    private val DEFAULT_DURATION: Long = 3000

    @Before
    fun setUp() {
        m_context = InstrumentationRegistry.getInstrumentation().context
        timeCounter = TimeCounter(Handler(Looper.getMainLooper()), m_mediaControllerAdapter)
        m_seekerBar = SeekerBar(m_context)
        m_seekerBarController2 = SeekerBarController2(m_mediaControllerAdapter, timeCounter)
        m_seekerBarController2.init(m_seekerBar)
        // set default metadata
        m_seekerBarController2.onMetadataChanged(createMetaData(DEFAULT_DURATION))
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testMetadataChanged() {
        val originalValueAnimator = m_seekerBarController2!!.valueAnimator
        val DURATION: Long = 1000
        m_seekerBarController2!!.onMetadataChanged(createMetaData(DURATION))
        assertValueAnimatorReset(originalValueAnimator)
        val resultValueAnimator = m_seekerBarController2!!.valueAnimator
        Assert.assertEquals(DURATION, resultValueAnimator!!.duration)
    }

    @Test
    fun testPlaybackSpeedIncreaseStatePaused() {
        val SPEED = 1.1f
        testSpeedChange(SPEED)
    }

    @Test
    fun testStartTracking() {
        //    m_seekerBar.setTimeCounter(timeCounter);
        m_seekerBarController2.onStartTrackingTouch(m_seekerBar)
        Assert.assertTrue(m_seekerBar.isTracking)
    }

    @Test
    fun testStopTracking() { //        TimeCounter timeCounter = mock(TimeCounter.class);
//        m_seekerBar.setTimeCounter(timeCounter);
        val valueAnimator = m_seekerBarController2!!.valueAnimator
        valueAnimator!!.start()
        m_seekerBarController2!!.onStopTrackingTouch(m_seekerBar!!)
        Assert.assertFalse(m_seekerBar!!.isTracking)
    }

    @Test
    fun testPlaybackSpeedDecreasedSpeedPaused() {
        val SPEED = 0.75f
        testSpeedChange(SPEED)
    }

    private fun testSpeedChange(speed: Float) {
        val EXPECTED_DURATION = (DEFAULT_DURATION / speed).toLong()
        val playbackState = createPlaybackState(PlaybackStateCompat.STATE_PAUSED, 350, speed)
        val originalValueAnimator = m_seekerBarController2!!.valueAnimator
        m_seekerBarController2.onPlaybackStateChanged(playbackState)
        assertValueAnimatorReset(originalValueAnimator)
        val valueAnimator = m_seekerBarController2!!.valueAnimator
        Assert.assertTrue(valueAnimator!!.isStarted)
        Assert.assertTrue(valueAnimator.isPaused)
        val resultDuration = valueAnimator.duration
        val errorMessage = StringBuilder().append("incorrect duration, expected: ")
                .append(EXPECTED_DURATION)
                .append(" but got ")
                .append(resultDuration)
                .toString()
        Assert.assertEquals(errorMessage, EXPECTED_DURATION, resultDuration)
    }

    private fun assertValueAnimatorReset(originalAnimator: ValueAnimator?) {
        val resultValueAnimator = m_seekerBarController2!!.valueAnimator
        Assert.assertNotEquals("Value animator should have been recreated", resultValueAnimator, originalAnimator)
    }

    private fun createMetaData(duration: Long): MediaMetadataCompat {
        return MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .build()
    }

    private fun createPlaybackState(state: Int, position: Int, speed: Float): PlaybackStateCompat {
        return PlaybackStateCompat.Builder()
                .setState(state, position.toLong(), speed)
                .build()
    }
}