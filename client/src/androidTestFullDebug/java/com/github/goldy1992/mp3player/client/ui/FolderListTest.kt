package com.github.goldy1992.mp3player.client.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock

class FolderListTest {

    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    @Mock
    val navController = mock<NavController>()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDisplayFolderList() {
        composeTestRule.setContent {
            
        }
    }

}