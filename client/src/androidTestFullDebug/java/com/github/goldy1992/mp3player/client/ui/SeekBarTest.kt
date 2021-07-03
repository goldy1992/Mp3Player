package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

/**
 * Test class for [SeekBar].
 */
class SeekBarTest {

    companion object {
        private const val PAUSED = PlaybackStateCompat.STATE_PAUSED
        private const val PLAYING = PlaybackStateCompat.STATE_PLAYING
    }

    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun firstTest() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val duration = 100000L
        val expectedDisplayedDuration = "01:40"
        val durationDescription = context.resources.getString(R.string.duration)
        val currentPosition = 10000L
        val currentPositionDescription = context.resources.getString(R.string.current_position)
        val expectedCurrentPosition = "00:10"
        val metadata = MediaMetadataCompat.Builder()
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
            .build()
        whenever(mockMediaController.metadata).thenReturn(MutableLiveData(metadata))
        val playbackState = PlaybackStateCompat.Builder()
            .setState(PAUSED, currentPosition, 1.0f)
            .build()
        whenever(mockMediaController.playbackState).thenReturn(MutableLiveData(playbackState))

        composeTestRule.setContent {
            SeekBar(mediaController = mockMediaController)
        }

        runBlocking {
            composeTestRule.awaitIdle()

        }
        composeTestRule.onNodeWithContentDescription(durationDescription)
            .assertExists()
            .assert(hasText(expectedDisplayedDuration))

       // composeTestRule.onNode().fetchSemanticsNode().config.
        composeTestRule.onNodeWithContentDescription(currentPositionDescription)
            .assertExists()
            .assert(hasText(expectedCurrentPosition))

    }

}