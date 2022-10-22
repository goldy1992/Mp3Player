package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.os.Bundle
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.media3.common.MediaMetadata
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.client.data.flows.player.PlaybackParametersFlow
import com.github.goldy1992.mp3player.commons.MetaDataKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Test class for [SeekBar].
 */
class SeekBarTest {

    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    val isPlayingFlow = mock<IsPlayingFlow>()
    val metadataFlow = mock<MetadataFlow>()
    val playbackParametersFlow = mock<PlaybackParametersFlow>()
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
        val extras = Bundle()
        extras.putLong(MetaDataKeys.DURATION, duration)
        val metadata = MediaMetadata.Builder()
            .setExtras(extras)
            .build()
        whenever(metadataFlow.state).thenReturn(MutableStateFlow(metadata))
//        val playbackState = PlaybackStateCompat.Builder()
//            .setState(PAUSED, currentPosition, 1.0f)
//            .build()
//        whenever(mockMediaController.playbackState).thenReturn(MutableLiveData(playbackState))

        composeTestRule.setContent {
            SeekBar(mediaController = mockMediaController,
                    metadataFlow = metadataFlow,
                    isPlayingState = isPlayingFlow,
                    playbackParametersFlow = playbackParametersFlow)
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