package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.PauseButton
import com.github.goldy1992.mp3player.client.ui.buttons.PlayButton
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

/**
 * Test class for [com.github.goldy1992.mp3player.client.ui.PlayToolbar].
 */
class PlayToolbarTest {

    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Tests that when the state of the [MediaControllerAdapter] playback state is
     * [PlaybackStateCompat.STATE_PAUSED] then the [PlayButton] is displayed.
     * When the [PlayButton] is clicked then [MediaControllerAdapter.pause] should be called
     */
    @Test
    fun testPlayButtonDisplayedWhenPaused() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.play)
        val isPlaying = false
        // Set Media to be NOT Playing
        whenever(mockMediaController.isPlaying).thenReturn(MutableLiveData(isPlaying))
        composeTestRule.setContent {
            PlayToolbar(mediaController = mockMediaController) {
                // do nothing
            }
        }
        composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true).assertExists()
        val playButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        playButton.assertExists()
        playButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            verify(mockMediaController, times(1)).play()
        }
    }
    /**
     * Tests that when the state of the [MediaControllerAdapter] playback state is
     * [PlaybackStateCompat.STATE_PLAYING] then the [PauseButton] is displayed.
     * When the [PauseButton] is clicked then [MediaControllerAdapter.play] should be called
     */
    @Test
    fun testPauseButtonDisplayedWhenPlaying() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.pause)
        val isPlaying = true
        // Set Media to be playing
        whenever(mockMediaController.isPlaying).thenReturn(MutableLiveData(isPlaying))
        composeTestRule.setContent {
            PlayToolbar(mediaController = mockMediaController) {
                // do nothing
            }
        }
        val pauseButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        pauseButton.assertExists()
        pauseButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            verify(mockMediaController, times(1)).pause()
        }

    }
}