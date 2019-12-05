package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.Constants
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class PlaybackSpeedControlsFragmentTest : FragmentTestBase<PlaybackSpeedControlsFragment>() {

    @Mock
    private lateinit var mediaControllerAdapter: MediaControllerAdapter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup(PlaybackSpeedControlsFragment::class.javaObjectType, true)
    }

    @Test
    fun testIncreaseSpeed() {
        val fragmentAction = FragmentAction { playbackSpeedControlsFragment: PlaybackSpeedControlsFragment? -> increaseSpeed(playbackSpeedControlsFragment) }
        performAction(fragmentAction)
    }

    @Test
    fun testDecreaseSpeed() {
        val fragmentAction = FragmentAction { playbackSpeedControlsFragment: PlaybackSpeedControlsFragment? -> decreaseSpeed(playbackSpeedControlsFragment) }
        performAction(fragmentAction)
    }

    private fun increaseSpeed(playbackSpeedControlsFragment: PlaybackSpeedControlsFragment?) {
        playbackSpeedControlsFragment!!.mediaControllerAdapter = mediaControllerAdapter
        playbackSpeedControlsFragment.mainUpdater = Handler(Looper.getMainLooper())
        playbackSpeedControlsFragment.increasePlaybackSpeed()
        Shadows.shadowOf(playbackSpeedControlsFragment.worker!!.looper).idle()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.sendCustomAction(ArgumentMatchers.eq(Constants.INCREASE_PLAYBACK_SPEED), ArgumentMatchers.any<Bundle>())
    }

    private fun decreaseSpeed(playbackSpeedControlsFragment: PlaybackSpeedControlsFragment?) {
        playbackSpeedControlsFragment!!.mediaControllerAdapter = mediaControllerAdapter
        playbackSpeedControlsFragment.mainUpdater = Handler(Looper.getMainLooper())
        playbackSpeedControlsFragment.decreasePlaybackSpeed()
        Shadows.shadowOf(playbackSpeedControlsFragment.worker!!.looper).idle()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.sendCustomAction(ArgumentMatchers.eq(Constants.DECREASE_PLAYBACK_SPEED), ArgumentMatchers.any<Bundle>())
    }
}