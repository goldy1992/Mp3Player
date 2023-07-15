package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferences
import com.github.goldy1992.mp3player.client.repositories.permissions.FakePermissionsRepository
import com.github.goldy1992.mp3player.client.repositories.preferences.FakeUserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.screens.settings.SettingsScreen
import com.github.goldy1992.mp3player.client.ui.screens.settings.SettingsScreenViewModel
import com.github.goldy1992.mp3player.commons.data.repositories.permissions.IPermissionsRepository
import com.github.goldy1992.mp3player.client.ui.components.ReportABugDialog
import com.github.goldy1992.mp3player.client.ui.components.AboutDialog

import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test class for [SettingsScreen].
 */
@ExperimentalMaterialApi
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val permissionsRepository : IPermissionsRepository = FakePermissionsRepository()

    private val userPreferencesRepository = FakeUserPreferencesRepository()

    private val viewModel = SettingsScreenViewModel(userPreferencesRepository = userPreferencesRepository,
    permissionsRepository = permissionsRepository)

    private lateinit var context : Context

    /**
     * Setup class.
     */
    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context

        userPreferencesRepository.useSystemDarkMode.value = false
        userPreferencesRepository.darkMode.value = false
    }

    /**
     * Tests that the dark mode switch is disabled when the switch to use System dark mode is enabled.
     */
    @Test
    fun testDarkModeDisabledWhenUseSystemDarkModeIsTrue() {
        // set system dark mode to be false
        val userPreferences = UserPreferences(systemDarkMode = true)
        userPreferencesRepository.userPreferences.value = userPreferences
        val systemDarkModeSwitch = context.getString(R.string.system_dark_mode_switch)
        val darkModeSwitch = context.getString(R.string.dark_mode_switch)
        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel)
        }
        composeTestRule.onNodeWithContentDescription(systemDarkModeSwitch).assertIsEnabled()
        composeTestRule.onNodeWithContentDescription(darkModeSwitch).assertIsNotEnabled()
    }

    /**
     * Tests that the dark mode switch is enabled when the switch to use System dark mode is disabled.
     */
    @Test
    fun testDarkModeEnabledWhenUseSystemDarkModeIsFalse() {
        // set system dark mode to be false
        userPreferencesRepository.useSystemDarkMode.value = false
        val systemDarkModeSwitch = context.getString(R.string.system_dark_mode_switch)
        val darkModeSwitch = context.getString(R.string.dark_mode_switch)

        composeTestRule.setContent {
            SettingsScreen(
                viewModel = viewModel)
        }
        composeTestRule.onNodeWithContentDescription(systemDarkModeSwitch).assertIsEnabled()
        composeTestRule.onNodeWithContentDescription(darkModeSwitch).assertIsEnabled()
    }

    /**
     * Tests that the [ReportABugDialog] is displayed when the Report a Bug Setting is clicked
     */
    @Test
    fun testReportABugFlow() {
        val lazyListDescription = context.getString(R.string.settings_screen_list_description)
        val reportABugText = context.getString(R.string.report_bug)
        val doneText = context.getString(R.string.done)
        composeTestRule.setContent {
            SettingsScreen(
                viewModel = viewModel)
        }
        composeTestRule.onNodeWithContentDescription(lazyListDescription).performScrollToNode (
            hasText(reportABugText)
        )
        composeTestRule.onNodeWithContentDescription(reportABugText).performClick()
        composeTestRule.onNodeWithText("GitHub").assertExists()

        composeTestRule.onNodeWithText(doneText).performClick()
        composeTestRule.onNodeWithText("GitHub").assertDoesNotExist()
    }

    /**
     * Tests that the [ReportABugDialog] is displayed when the Report a Bug Setting is clicked
     */
    @Test
    fun testFeatureRequestFlow() {
        val lazyListDescription = context.getString(R.string.settings_screen_list_description)
        val requestFeatureText = context.getString(R.string.request_feature)
        val doneText = context.getString(R.string.done)
        composeTestRule.setContent {
            SettingsScreen(
                viewModel = viewModel)
        }
        composeTestRule.onNodeWithContentDescription(lazyListDescription).performScrollToNode (
            hasText(requestFeatureText)
        )
        composeTestRule.onNodeWithContentDescription(requestFeatureText).performClick()
        composeTestRule.onNodeWithText("GitHub").assertExists()

        composeTestRule.onNodeWithText(doneText).performClick()
        composeTestRule.onNodeWithText("GitHub").assertDoesNotExist()
    }

    /**
     * Tests that the [AboutDialog] is displayed correctly when selected from the Settings Menu
     */
    @Test
    fun testDisplayAboutDialog() {
        val lazyListDescription = context.getString(R.string.settings_screen_list_description)
        val aboutMenuItemTitle = context.getString(R.string.about)
        val aboutDialogText = context.getString(R.string.about_description)
        val closeButtonDescription = context.getString(R.string.close)
        composeTestRule.setContent {
            SettingsScreen(
                viewModel = viewModel)
        }
        composeTestRule.onNodeWithContentDescription(lazyListDescription).performScrollToNode (
            hasText(aboutMenuItemTitle)
        )
        composeTestRule.onNodeWithText(aboutMenuItemTitle).performClick()
        composeTestRule.onNodeWithText(aboutDialogText).assertExists()
        composeTestRule.onNodeWithContentDescription(closeButtonDescription).performClick()
        composeTestRule.onNodeWithText(aboutDialogText).assertDoesNotExist()
    }

}