package com.github.goldy1992.mp3player.client

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import kotlinx.coroutines.runBlocking


/**
 * Helper object to assert whether the [PlayPauseButton] is playing or not
 */
object PlayPauseButtonTestUtils {

    fun assertNotPlaying(composeTestRule : ComposeTestRule, context : Context) {
        val playButtonContentDescription = context.getString(R.string.play)
        composeTestRule.onNodeWithContentDescription(playButtonContentDescription).assertExists()
    }

    fun assertIsPlaying(composeTestRule: ComposeTestRule, context: Context) {
        val pauseButtonContentDescription = context.getString(R.string.pause)
        composeTestRule.onNodeWithContentDescription(pauseButtonContentDescription)
    }

    fun waitForPlaying(composeTestRule: ComposeTestRule, context: Context) {
        val pauseButtonContentDescription = context.getString(R.string.pause)
        composeTestRule.waitUntil(1000L) {
            try {
                composeTestRule.onNodeWithContentDescription(pauseButtonContentDescription).assertExists()
                true
            } catch (assertionError: AssertionError) {
                false
            }
        }
    }

    fun clickPlay(composeTestRule : ComposeTestRule, context: Context) {
        val playButtonContentDescription = context.getString(R.string.play)
        composeTestRule.onNodeWithContentDescription(playButtonContentDescription).performClick()
    }

    fun clickPause(composeTestRule : ComposeTestRule, context: Context) {
        runBlocking {
            val pauseButtonContentDescription = context.getString(R.string.pause)
            composeTestRule.onNodeWithContentDescription(pauseButtonContentDescription)
                .performClick()
        }
    }

}