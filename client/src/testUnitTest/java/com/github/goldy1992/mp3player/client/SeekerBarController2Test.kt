package com.github.goldy1992.mp3player.client

import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.views.TimeCounter
import com.google.android.material.slider.Slider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
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
    private lateinit var context: Context

    private val mediaControllerAdapter: MediaControllerAdapter = mock<MediaControllerAdapter>()
    private val timeCounter: TimeCounter = mock<TimeCounter>()
    private lateinit var seekerBar: Slider

    private lateinit var seekerBarController2: SeekerBarController2
    private val defaultDuration: Long = 3000

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        context.setTheme(R.style.AppTheme)
        seekerBar = Slider(context)
        seekerBar.valueTo = defaultDuration.toFloat()
        seekerBarController2 = SeekerBarController2(mediaControllerAdapter, timeCounter)
        seekerBarController2.init(seekerBar)
    }

    @Test
    fun testMetadataChanged() {
        val expectedDuration: Long = 1000
        seekerBarController2.onChanged(createMetaData(expectedDuration))
        assertEquals(expectedDuration.toFloat(), seekerBar.valueTo)
    }

    @Test
    fun testStopTracking() {
        val expectedSeekToValue = 25
        this.seekerBar.value = expectedSeekToValue.toFloat()
        seekerBarController2.onStopTrackingTouch(seekerBar)
        verify(mediaControllerAdapter, times(1)).seekTo(expectedSeekToValue.toLong())
    }

    @Test
    fun testStartTracking() {
        seekerBarController2.onStartTrackingTouch(seekerBar)
        verify(timeCounter, times(1)).cancelTimerDuringTracking()
    }

    private fun createMetaData(duration: Long): MediaMetadataCompat {
        return MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .build()
    }
}