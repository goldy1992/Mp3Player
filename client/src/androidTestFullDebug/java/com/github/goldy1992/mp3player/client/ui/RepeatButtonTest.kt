package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatAllButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatNoneButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatOneButton
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

/**
 * Test class for the [RepeatButton]
 */
class RepeatButtonTest {

    companion object {
        private const val REPEAT_ONE = PlaybackStateCompat.REPEAT_MODE_ONE
        private const val REPEAT_ALL = PlaybackStateCompat.REPEAT_MODE_ALL
        private const val REPEAT_NONE = PlaybackStateCompat.REPEAT_MODE_NONE
    }

    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Repeat Mode is
     * [PlaybackStateCompat.REPEAT_MODE_ONE], then the [RepeatOneButton] should be displayed.
     * When the [RepeatOneButton] is clicked then [MediaControllerAdapter.setRepeatMode] should be
     * called with the argument [PlaybackStateCompat.REPEAT_MODE_ALL]
     */
    @Test
    fun testRepeatOneModeShowsRepeatOneButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_one)
        // Set Repeat Mode One
        whenever(mockMediaController.repeatMode).thenReturn(MutableLiveData(REPEAT_ONE))
        composeTestRule.setContent {
            RepeatButton(mediaController = mockMediaController)
        }

        val repeatOneButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatOneButton.assertExists()
        repeatOneButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            verify(mockMediaController, times(1)).setRepeatMode(REPEAT_ALL)
        }
    }

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Repeat Mode is
     * [PlaybackStateCompat.REPEAT_MODE_NONE], then the [RepeatNoneButton] should be displayed.
     * When the [RepeatNoneButton] is clicked then [MediaControllerAdapter.setRepeatMode] should be
     * called with the argument [PlaybackStateCompat.REPEAT_MODE_ALL]
     */
    @Test
    fun testRepeatNoneModeShowsRepeatNoneButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_none)
        // Set Repeat Mode One
        whenever(mockMediaController.repeatMode).thenReturn(MutableLiveData(REPEAT_NONE))
        composeTestRule.setContent {
            RepeatButton(mediaController = mockMediaController)
        }

        val repeatNoneButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatNoneButton.assertExists()
        repeatNoneButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            verify(mockMediaController, times(1)).setRepeatMode(REPEAT_ONE)
        }
    }

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Repeat Mode is
     * [PlaybackStateCompat.REPEAT_MODE_ALL], then the [RepeatAllButton] should be displayed.
     * When the [RepeatAllButton] is clicked then [MediaControllerAdapter.setRepeatMode] should be
     * called with the argument [PlaybackStateCompat.REPEAT_MODE_NONE]
     */
    @Test
    fun testRepeatAllModeShowsRepeatAllButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_all)
        // Set Repeat Mode One
        whenever(mockMediaController.repeatMode).thenReturn(MutableLiveData(REPEAT_ALL))
        composeTestRule.setContent {
            RepeatButton(mediaController = mockMediaController)
        }

        val repeatAllButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatAllButton.assertExists()
        repeatAllButton.performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            verify(mockMediaController, times(1)).setRepeatMode(REPEAT_NONE)
        }
    }


}