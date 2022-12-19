package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

/**
 * Test class for [PlayToolbar].
 */
class PlayToolbarTest {

    private val mockOnClick : MockOnClick = mock()

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
        composeTestRule.setContent {
            PlayToolbar(isPlayingProvider = { isPlaying },
                        onClickPlay = { mockOnClick.onClick() })
        }

        composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true).assertExists()
        val playButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        playButton.assertExists()
        playButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            verify(mockOnClick, times(1)).onClick()
        }
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
        composeTestRule.setContent {
            PlayToolbar(isPlayingProvider = { isPlaying },
                    onClickPause = { mockOnClick.onClick() })
        }
        val pauseButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        pauseButton.assertExists()
        pauseButton.performClick()
        verify(mockOnClick, times(1)).onClick()
    }

    /**
     * Ensures that the onClick function is called as expected
     */
    @Test
    fun testOnClick() {
        val bottomAppBarDescr = InstrumentationRegistry.getInstrumentation().context.getString(R.string.bottom_app_bar)

        composeTestRule.setContent {
            PlayToolbar(onClickBar = { mockOnClick.onClick()})
        }
        composeTestRule.onNodeWithContentDescription(bottomAppBarDescr).performTouchInput {
            this.click(this.percentOffset(0.9f, 0.9f))
        }
        verify(mockOnClick, times(1)).onClick()
    }

    private class MockOnClick {
        fun onClick(){
        }
    }
}