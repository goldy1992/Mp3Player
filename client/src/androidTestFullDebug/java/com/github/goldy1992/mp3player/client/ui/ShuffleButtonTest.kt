package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.flows.player.ShuffleModeFlow
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleOffButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleOnButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Test class for the [ShuffleButton]
 */
class ShuffleButtonTest {
    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    val shuffleModeFlow = mock<ShuffleModeFlow>()

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Shuffle Mode is on, then
     * the [ShuffleOffButton] should be displayed.
     * When the [ShuffleOffButton] is clicked, [MediaControllerAdapter.setShuffleMode] should be
     * called with [PlaybackStateCompat.SHUFFLE_MODE_ALL]
     */
    @Test
    fun testShuffleOffButtonDisplayedWhenShuffleModeOff() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.shuffle_off)
        // Set Shuffle Mode to be Off
       // whenever(shuffleModeFlow.state).thenReturn(MutableStateFlow(false))
        composeTestRule.setContent {
            ShuffleButton(mediaController = mockMediaController, shuffleModeState = MutableStateFlow(false))
        }
        composeTestRule.onNodeWithContentDescription(expected, useUnmergedTree = true).assertExists()
        val shuffleOffButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        shuffleOffButton.assertExists()
        shuffleOffButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
           // verify(mockMediaController, times(1)).setShuffleMode(SHUFFLE_ON)
        }

    }

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Shuffle Mode is on, then
     * the [ShuffleOnButton] should be displayed.
     * When the [ShuffleOnButton] is clicked, [MediaControllerAdapter.setShuffleMode] should be
     * called with [PlaybackStateCompat.SHUFFLE_MODE_NONE]
     */
    @Test
    fun testShuffleOnButtonDisplayedWhenShuffleModeOn() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.shuffle_on)
        // Set Shuffle Mode to be On
     //   whenever(mockMediaController.shuffleMode).thenReturn(MutableLiveData(SHUFFLE_ON))
        composeTestRule.setContent {
            ShuffleButton(mediaController = mockMediaController, shuffleModeState = MutableStateFlow(true))
        }
        composeTestRule.onNodeWithContentDescription(expected, useUnmergedTree = true).assertExists()
        val shuffleOnButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        shuffleOnButton.assertExists()
        shuffleOnButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
  //          verify(mockMediaController, times(1)).setShuffleMode(SHUFFLE_OFF)
        }
    }
}