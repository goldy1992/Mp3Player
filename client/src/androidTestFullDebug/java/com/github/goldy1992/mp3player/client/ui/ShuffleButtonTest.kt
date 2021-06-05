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
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleOffButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleOnButton
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

class ShuffleButtonTest {

    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Shuffle Mode is off, then
     * the [ShuffleOnButton] should be displayed
     */
    @Test
    fun testShuffleOnButtonDisplayedWhenShuffleModeOff() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.shuffle_on)
        // Set Shuffle Mode to be Off
        whenever(mockMediaController.shuffleMode).thenReturn(MutableLiveData(PlaybackStateCompat.SHUFFLE_MODE_NONE))
        composeTestRule.setContent {
            ShuffleButton(mediaController = mockMediaController)
        }
        composeTestRule.onNodeWithContentDescription(expected, useUnmergedTree = true).assertExists()
    }

    /**
     * Tests that when the state of the [MediaControllerAdapter] says that Shuffle Mode is on, then
     * the [ShuffleOffButton] should be displayed
     */
    @Test
    fun testShuffleOffButtonDisplayedWhenShuffleModeOn() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = context.resources.getString(R.string.shuffle_off)
        // Set Shuffle Mode to be On
        whenever(mockMediaController.shuffleMode).thenReturn(MutableLiveData(PlaybackStateCompat.SHUFFLE_MODE_ALL))
        composeTestRule.setContent {
            ShuffleButton(mediaController = mockMediaController)
        }
        composeTestRule.onNodeWithContentDescription(expected, useUnmergedTree = true).assertExists()
    }
}