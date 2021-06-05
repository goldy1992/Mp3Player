package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleButton
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

class ShuffleButtonTest {

    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Playback in NOT playing
     * then the Play button is displayed.
     */
    @Test
    fun testShuffleOnButtonDisplayedWhenShuffleModeOff() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.shuffle_on)
        // Set Media to be NOT Playing
        whenever(mockMediaController.shuffleMode).thenReturn(MutableLiveData(PlaybackStateCompat.SHUFFLE_MODE_NONE))
        composeTestRule.setContent {
            ShuffleButton(mediaController = mockMediaController)
        }

        runBlocking {
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithContentDescription(expected).assertExists()
        }


    }
}