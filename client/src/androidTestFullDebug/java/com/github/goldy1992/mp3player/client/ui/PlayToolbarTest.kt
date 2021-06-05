package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
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
     * Tests that when the state of the [MediaControllerAdapter] says that Playback in NOT playing
     * then the Play button is displayed.
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
    }
    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Playback in playing
     * then the Pause button is displayed.
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
        composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true).assertExists()
    }
}