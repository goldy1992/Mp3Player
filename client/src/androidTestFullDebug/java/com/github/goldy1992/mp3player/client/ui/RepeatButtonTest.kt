package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.media3.common.Player.*
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.flows.player.RepeatModeFlow
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatAllButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatNoneButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatOneButton
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
 * Test class for the [RepeatButton]
 */
class RepeatButtonTest {


    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    private val repeatModeFlow = mock<RepeatModeFlow>()

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Repeat Mode is
     * [REPEAT_MODE_ONE], then the [RepeatOneButton] should be displayed.
     * When the [RepeatOneButton] is clicked then [MediaControllerAdapter.setRepeatMode] should be
     * called with the argument [REPEAT_MODE_ALL]
     */
    @Test
    fun testRepeatOneModeShowsRepeatOneButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_one)
        // Set Repeat Mode One
        whenever(repeatModeFlow.state).thenReturn(MutableStateFlow(REPEAT_MODE_ONE))
        composeTestRule.setContent {
            RepeatButton(mediaController = mockMediaController,
                        repeatModeState = repeatModeFlow)
        }

        val repeatOneButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatOneButton.assertExists()
        repeatOneButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            verify(mockMediaController, times(1)).setRepeatMode(REPEAT_MODE_ALL)
        }
    }

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Repeat Mode is
     * [REPEAT_MODE_OFF], then the [RepeatNoneButton] should be displayed.
     * When the [RepeatNoneButton] is clicked then [MediaControllerAdapter.setRepeatMode] should be
     * called with the argument [REPEAT_MODE_ALL]
     */
    @Test
    fun testRepeatNoneModeShowsRepeatNoneButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_none)
        // Set Repeat Mode One
        whenever(repeatModeFlow.state).thenReturn(MutableStateFlow(REPEAT_MODE_OFF))
        composeTestRule.setContent {
            RepeatButton(mediaController = mockMediaController,
                        repeatModeState = repeatModeFlow)
        }

        val repeatNoneButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatNoneButton.assertExists()
        repeatNoneButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            verify(mockMediaController, times(1)).setRepeatMode(REPEAT_MODE_ONE)
        }
    }

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Repeat Mode is
     * [REPEAT_MODE_ALL], then the [RepeatAllButton] should be displayed.
     * When the [RepeatAllButton] is clicked then [MediaControllerAdapter.setRepeatMode] should be
     * called with the argument [REPEAT_MODE_OFF]
     */
    @Test
    fun testRepeatAllModeShowsRepeatAllButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_all)
        // Set Repeat Mode One
        whenever(repeatModeFlow.state).thenReturn(MutableStateFlow(REPEAT_MODE_ALL))
        composeTestRule.setContent {
            RepeatButton(mediaController = mockMediaController,
                        repeatModeState = repeatModeFlow)
        }

        val repeatAllButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatAllButton.assertExists()
        repeatAllButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            verify(mockMediaController, times(1)).setRepeatMode(REPEAT_MODE_OFF)
        }
    }


}