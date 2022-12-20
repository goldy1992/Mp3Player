package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleOffButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleOnButton
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Test class for the [ShuffleButton]
 */
class ShuffleButtonTest {

    private val mockOnClick = mock<MockOnClick>()

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * WHEN: the shuffle mode is DISABLED
     * THEN: the [ShuffleOffButton] should be displayed.
     * AND WHEN: the [ShuffleOffButton] is clicked,
     * THEN: The onClick function should be called to ENABLE shuffle
     */
    @Test
    fun testShuffleOffButtonDisplayedWhenShuffleModeOff() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.shuffle_off)
        val shuffleEnabled = false
        // Set Shuffle Mode to be Off
        composeTestRule.setContent {
            ShuffleButton(
                shuffleEnabledProvider = { shuffleEnabled },
                onClick = {mockOnClick.onClick(it) }
            )
        }
        composeTestRule.onNodeWithContentDescription(expected, useUnmergedTree = true).assertExists()
        val shuffleOffButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        shuffleOffButton.assertExists()
        shuffleOffButton.performClick()
        verify(mockOnClick).onClick(true)

    }

    /**
     * WHEN: the shuffle mode is ENABLED
     * THEN: the [ShuffleOnButton] should be displayed.
     * AND WHEN: the [ShuffleOnButton] is clicked,
     * THEN: The onClick function should be called to DISABLE shuffle
     */
    @Test
    fun testShuffleOnButtonDisplayedWhenShuffleModeOn() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.shuffle_on)
        // Set Shuffle Mode to be On
        val shuffleEnabled = true
        composeTestRule.setContent {
            ShuffleButton(
                shuffleEnabledProvider = { shuffleEnabled },
                onClick = { mockOnClick.onClick(it) }
            )
        }
        composeTestRule.onNodeWithContentDescription(expected, useUnmergedTree = true).assertExists()
        val shuffleOnButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        shuffleOnButton.assertExists()
        shuffleOnButton.performClick()
        verify(mockOnClick).onClick(false)
    }

    private class MockOnClick {
        fun onClick(shuffleEnabled : Boolean) { }
    }
}