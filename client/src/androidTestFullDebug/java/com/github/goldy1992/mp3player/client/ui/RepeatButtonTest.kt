package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.RepeatMode
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatButton
import org.junit.Rule
import org.junit.Test

/**
 * Test class for the [RepeatButton]
 */
class RepeatButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * WHEN: the repeat mode state is [RepeatMode.ONE]
     * THEN: the RepeatModeOne button is displayed.
     * AND WHEN: The RepeatModeOne button is clicked:
     * THEN: onClick is called with [RepeatMode.ONE]
     */
    @Test
    fun testRepeatOneModeShowsRepeatOneButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_one)
        composeTestRule.setContent {
            RepeatButton(repeatModeProvider = { RepeatMode.ONE } )
        }

        val repeatOneButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatOneButton.assertExists()
    }
    /**
     * WHEN: the repeat mode state is [RepeatMode.OFF]
     * THEN: the RepeatModeOne button is displayed.
     * AND WHEN: The RepeatModeOne button is clicked:
     * THEN: onClick is called with [RepeatMode.OFF]
     */
    @Test
    fun testRepeatOffModeShowsRepeatOffButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_none)
        // Set Repeat Mode Off
        composeTestRule.setContent {
            RepeatButton(repeatModeProvider = { RepeatMode.OFF } )
        }
        val repeatOffButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatOffButton.assertExists()
    }

    /**
     * WHEN: the repeat mode state is [RepeatMode.ALL]
     * THEN: the RepeatModeOne button is displayed.
     * AND WHEN: The RepeatModeOne button is clicked:
     * THEN: onClick is called with [RepeatMode.ALL]
     */
    @Test
    fun testRepeatAllModeShowsRepeatAllButton() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.repeat_all)
        // Set Repeat Mode One
        composeTestRule.setContent {
            RepeatButton(repeatModeProvider = { RepeatMode.ALL } )
        }

        val repeatAllButton = composeTestRule.onNode(hasContentDescription(expected), useUnmergedTree = true)
        repeatAllButton.assertExists()
    }
}