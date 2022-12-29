package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.os.Bundle
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.media3.common.MediaMetadata
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.seekbar.SeekBar
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.commons.MetaDataKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Test class for [SeekBar].
 */
class SeekBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockSeekTo = mock<MockSeekTo>()

    @Before
    fun setup() { }

    @Test
    fun testSeekBarDisplaysCorrectly() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val duration = 100000L
        val expectedDisplayedDuration = "01:40"
        val durationDescription = context.resources.getString(R.string.duration)
        val currentPosition = 10000L
        val currentPositionDescription = context.resources.getString(R.string.current_position)
        val expectedCurrentPosition = "00:10"
        val extras = Bundle()
        extras.putLong(MetaDataKeys.DURATION, duration)
        val metadata = MediaMetadata.Builder()
            .setExtras(extras)
            .build()

        composeTestRule.setContent {
            SeekBar(
                isPlayingProvider = {  false },
                metadataProvider =  {metadata },
                playbackSpeedProvider = { 1.0f },
                playbackPositionProvider ={ PlaybackPositionEvent(false, currentPosition, 0L) },
                seekTo = { mockSeekTo.seekTo(it) }
            )
        }

        composeTestRule.onNodeWithContentDescription(durationDescription)
            .assertExists()
            .assert(hasText(expectedDisplayedDuration))

        composeTestRule.onNodeWithContentDescription(currentPositionDescription)
            .assertExists()
            .assert(hasText(expectedCurrentPosition))

    }

    private class MockSeekTo {
        fun seekTo(value : Long) {}
    }

}