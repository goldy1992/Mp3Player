package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.Constants
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class PlaybackSpeedControlsFragmentTest : FragmentTestBase<PlaybackSpeedControlsFragment>() {

    private val mediaControllerAdapter : MediaControllerAdapter = mock<MediaControllerAdapter>()


    @Before
    fun setup() {
        super.setup(PlaybackSpeedControlsFragment::class.javaObjectType, true)
    }

    @Test
    fun testIncreaseSpeed() {
        val fragmentAction = FragmentAction { playbackSpeedControlsFragment: PlaybackSpeedControlsFragment? -> increaseSpeed(playbackSpeedControlsFragment) }
       // performAction(fragmentAction)
    }

    @Test
    fun testDecreaseSpeed() {
        val fragmentAction = FragmentAction { playbackSpeedControlsFragment: PlaybackSpeedControlsFragment? -> decreaseSpeed(playbackSpeedControlsFragment) }
       // performAction(fragmentAction)
    }

    private fun increaseSpeed(playbackSpeedControlsFragment: PlaybackSpeedControlsFragment?) {
        playbackSpeedControlsFragment!!.mediaControllerAdapter = mediaControllerAdapter
        playbackSpeedControlsFragment.increasePlaybackSpeed()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(mediaControllerAdapter, times(1)).sendCustomAction(eq(Constants.INCREASE_PLAYBACK_SPEED), any<Bundle>())
    }

    private fun decreaseSpeed(playbackSpeedControlsFragment: PlaybackSpeedControlsFragment?) {
        playbackSpeedControlsFragment!!.mediaControllerAdapter = mediaControllerAdapter
        playbackSpeedControlsFragment.decreasePlaybackSpeed()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(mediaControllerAdapter, times(1)).sendCustomAction(eq(Constants.DECREASE_PLAYBACK_SPEED), any<Bundle>())
    }
}