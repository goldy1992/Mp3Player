package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.ui.components.seekbar.SeekBar
import org.junit.Rule
import org.junit.Test

/**
 * Test class for [SeekBar].
 */
class SeekBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSeekBarDisplaysCorrectly() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val duration = 100000L
        val expectedDisplayedDuration = "01:40"
        val durationDescription = context.resources.getString(R.string.duration)
        val currentPosition = 10000L
        val currentPositionDescription = context.resources.getString(R.string.current_position)
        val expectedCurrentPosition = "00:10"
        val song = Song(duration = duration)

        composeTestRule.setContent {
            SeekBar(
                isPlayingProvider = {  false },
                currentSongProvider =  { song },
                playbackSpeedProvider = { 1.0f },
                playbackPositionProvider ={ PlaybackPositionEvent(false, currentPosition, 0L) },
            )
        }

        composeTestRule.onNodeWithContentDescription(durationDescription)
            .assertExists()
            .assert(hasText(expectedDisplayedDuration))

        composeTestRule.onNodeWithContentDescription(currentPositionDescription)
            .assertExists()
            .assert(hasText(expectedCurrentPosition))

    }



}