package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.media3.common.Player.*
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatButton
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

/**
 * Test class for the [RepeatButton]
 */
class RepeatButtonTest {

    private val mockOnClick = mock<MockOnClick>()

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * WHEN: the repeat mode state is [REPEAT_MODE_ONE]
     * THEN: the RepeatModeOne button is displayed.
     * AND WHEN: The RepeatModeOne button is clicked:
     * THEN: onClick is called with [REPEAT_MODE_ONE]
     */
    @Test
    fun testRepeatOneModeShowsRepeatOneButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_one)
        composeTestRule.setContent {
            RepeatButton(repeatModeProvider = { REPEAT_MODE_ONE },
                        onClick = { mockOnClick.onClick(it)}
            )
        }

        val repeatOneButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatOneButton.assertExists()
        repeatOneButton.performClick()
        verify(mockOnClick).onClick(REPEAT_MODE_ONE)
    }
    /**
     * WHEN: the repeat mode state is [REPEAT_MODE_OFF]
     * THEN: the RepeatModeOne button is displayed.
     * AND WHEN: The RepeatModeOne button is clicked:
     * THEN: onClick is called with [REPEAT_MODE_OFF]
     */
    @Test
    fun testRepeatOffModeShowsRepeatOffButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_none)
        // Set Repeat Mode Off
        composeTestRule.setContent {
            RepeatButton(repeatModeProvider = { REPEAT_MODE_OFF },
                onClick = { mockOnClick.onClick(it)}
            )
        }
        val repeatOffButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatOffButton.assertExists()
        repeatOffButton.performClick()
        verify(mockOnClick).onClick(REPEAT_MODE_OFF)
    }

    /**
     * WHEN: the repeat mode state is [REPEAT_MODE_ALL]
     * THEN: the RepeatModeOne button is displayed.
     * AND WHEN: The RepeatModeOne button is clicked:
     * THEN: onClick is called with [REPEAT_MODE_ALL]
     */
    @Test
    fun testRepeatAllModeShowsRepeatAllButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_all)
        // Set Repeat Mode One
        composeTestRule.setContent {
            RepeatButton(repeatModeProvider = { REPEAT_MODE_ALL },
                onClick = { mockOnClick.onClick(it)}
            )
        }

        val repeatAllButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatAllButton.assertExists()
        repeatAllButton.performClick()
        verify(mockOnClick, times(1)).onClick(REPEAT_MODE_ALL)
    }

    private class MockOnClick {
        fun onClick(@RepeatMode repeatMode: Int) { }
    }

}