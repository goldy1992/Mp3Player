package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test class for [SearchScreen].
 */
class SearchScreenTest : MediaTestBase(){

    private val mockMediaRepo : MediaRepository = MediaRepository(MutableLiveData())

    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @Before
    override fun setup() {
        super.setup()
        this.context = InstrumentationRegistry.getInstrumentation().context
        composeTestRule.setContent {
            SearchScreen(
                navController = mockNavController,
                mediaBrowser = mockMediaBrowser,
                mediaController = mockMediaController,
                mediaRepository = mockMediaRepo
            )
        }
    }

    @Test
    fun test1() {
        assertTrue(true)
    }

}