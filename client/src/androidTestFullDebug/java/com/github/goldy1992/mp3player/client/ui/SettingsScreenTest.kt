package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferences
import com.github.goldy1992.mp3player.client.repositories.permissions.FakePermissionsRepository
import com.github.goldy1992.mp3player.client.repositories.preferences.FakeUserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.screens.settings.SettingsScreen
import com.github.goldy1992.mp3player.client.ui.screens.settings.SettingsScreenViewModel
import com.github.goldy1992.mp3player.client.utils.VersionUtils
import com.github.goldy1992.mp3player.commons.data.repositories.permissions.IPermissionsRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Test class for [SettingsScreen].
 */
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mock<NavController>()

    private val permissionsRepository : IPermissionsRepository = FakePermissionsRepository()

    private val userPreferencesRepository = FakeUserPreferencesRepository()

    private val versionUtils = mock<VersionUtils>()

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
        whenever(versionUtils.getAppVersion()).thenReturn("Version")
    }

    /**
     * Tests that the dark mode switch is disabled when the switch to use System dark mode is enabled.
     */
    @ExperimentalMaterialApi
    @Test
    fun testDarkModeDisabledWhenUseSystemDarkModeIsTrue() {
        // set system dark mode to be false
        val userPreferences = UserPreferences(systemDarkMode = true)
        userPreferencesRepository.userPreferences.value = userPreferences
        val systemDarkModeSwitch = context.getString(R.string.system_dark_mode_switch)
        val darkModeSwitch = context.getString(R.string.dark_mode_switch)
        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel,
                navController = navController)
        }
        composeTestRule.onNodeWithContentDescription(systemDarkModeSwitch).assertIsEnabled()
        composeTestRule.onNodeWithContentDescription(darkModeSwitch).assertIsNotEnabled()
    }

    /**
     * Tests that the dark mode switch is enabled when the switch to use System dark mode is disabled.
     */
    @ExperimentalMaterialApi
    @Test
    fun testDarkModeEnabledWhenUseSystemDarkModeIsFalse() {
        // set system dark mode to be false
        userPreferencesRepository.useSystemDarkMode.value = false
        val systemDarkModeSwitch = context.getString(R.string.system_dark_mode_switch)
        val darkModeSwitch = context.getString(R.string.dark_mode_switch)

        composeTestRule.setContent {
            SettingsScreen(
                viewModel = viewModel,
                navController = navController)
        }
        composeTestRule.onNodeWithContentDescription(systemDarkModeSwitch).assertIsEnabled()
        composeTestRule.onNodeWithContentDescription(darkModeSwitch).assertIsEnabled()
    }

}