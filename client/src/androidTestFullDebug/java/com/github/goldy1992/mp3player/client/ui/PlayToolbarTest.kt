package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.ui.buttons.PauseButton
import com.github.goldy1992.mp3player.client.ui.buttons.PlayButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Test class for [com.github.goldy1992.mp3player.client.ui.PlayToolbar].
 */
class PlayToolbarTest {

    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    val isPlayingFlow = mock<IsPlayingFlow>()

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
        //  whenever(isPlayingFlow.state).thenReturn(MutableStateFlow(isPlaying))
        composeTestRule.setContent {
            PlayToolbar(mediaController = mockMediaController,
                        isPlayingState = MutableStateFlow(isPlaying)
            ) {
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
       // whenever(isPlayingFlow.state).thenReturn(MutableStateFlow(isPlaying))
        composeTestRule.setContent {
            PlayToolbar(mediaController = mockMediaController,
                        isPlayingState = MutableStateFlow(isPlaying)
            ) {
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

    /**
     * Ensures that the onClick function is called as expected
     */
    @Test
    fun testOnClick() {
        val bottomAppBarDescr = InstrumentationRegistry.getInstrumentation().context.getString(R.string.bottom_app_bar)
        val mockOnClick = mock<MockOnClick>()
        composeTestRule.setContent {
            PlayToolbar(mediaController = mockMediaController, isPlayingState = MutableStateFlow(true), onClick = mockOnClick::onClick)
        }
        composeTestRule.onNodeWithContentDescription(bottomAppBarDescr).performTouchInput {
            this.click(this.percentOffset(0.9f, 0.9f))
        }

        runBlocking {
            composeTestRule.awaitIdle()
        }

        verify(mockOnClick, times(1)).onClick()
    }

    private class MockOnClick() {
        fun onClick(){
        }
    }
}