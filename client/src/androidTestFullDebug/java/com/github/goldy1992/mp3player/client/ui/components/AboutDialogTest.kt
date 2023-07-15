package com.github.goldy1992.mp3player.client.ui.components

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AboutDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context : Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }
    @Test
    fun testDarkModeDisplay() {
        val aboutDialogText = context.getString(R.string.about_description)
        val githubDarkIconDescription = context.getString(R.string.github_dark_mode_icon_description)

        composeTestRule.setContent {
            AboutDialog(darkMode = true)
        }
        composeTestRule.onNodeWithText(aboutDialogText).assertExists()
        composeTestRule.onNodeWithContentDescription(githubDarkIconDescription).assertExists()
    }
}