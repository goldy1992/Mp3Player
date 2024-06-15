package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTouchInput
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.media.MediaActions
import com.github.goldy1992.mp3player.client.models.media.PlaybackState
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.SharedElementComposable
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Test class for [PlayToolbar].
 */
class PlayToolbarTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * WHEN: the playback state IS paused
     * THEN: the play button is displayed.
     * AND WHEN: The play button is clicked:
     * THEN: onPlay is invoked.
     */
    @Test
    fun testPlayButtonDisplayedWhenPaused() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.play)
        val isPlaying = false
        val playbackState = PlaybackState(isPlaying = isPlaying)
        composeTestRule.setContent {
            SharedElementComposable { animatedVisibilityScope ->
                PlayToolbar(
                    animatedVisibilityScope = animatedVisibilityScope,
                    playbackState = playbackState
                )
            }
        }
        composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true).assertExists()
        val playButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        playButton.assertExists()
    }
    /**
     * WHEN: the playback state IS playing
     * THEN: the pause button is displayed.
     * AND WHEN: The pause button is clicked:
     * THEN: onPause is invoked.
     */
    @Test
    fun testPauseButtonDisplayedWhenPlaying() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.pause)
        val isPlaying = true
        val playbackState = PlaybackState(isPlaying = isPlaying)
        composeTestRule.setContent {
            SharedElementComposable { animatedVisibilityScope ->
                PlayToolbar(
                    animatedVisibilityScope = animatedVisibilityScope,
                    playbackState = playbackState
                )
            }
        }
        val pauseButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        pauseButton.assertExists()
    }

    /**
     * Ensures that the onClick function is called as expected
     */
    @Test
    fun testOnClick() {
        val bottomAppBarDescr = InstrumentationRegistry.getInstrumentation().context.getString(R.string.bottom_app_bar)
        val mockOnClick = MockOnClick()
        composeTestRule.setContent {
            SharedElementComposable { animatedVisibilityScope ->
                PlayToolbar(
                    animatedVisibilityScope = animatedVisibilityScope,
                    playbackState = PlaybackState.DEFAULT,
                    onClickBar = { mockOnClick.onClick() }
                )
            }
        }
        composeTestRule.onNodeWithContentDescription(bottomAppBarDescr).performTouchInput {
            this.click(this.percentOffset(0.9f, 0.9f))
        }
        assertEquals(1, mockOnClick.numberOfClicks)


    }

    private class MockOnClick {
        var numberOfClicks = 0
        fun onClick(){
            numberOfClicks++
        }
    }
}