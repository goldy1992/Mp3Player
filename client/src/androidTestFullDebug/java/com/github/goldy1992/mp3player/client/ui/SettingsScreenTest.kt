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
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.utils.VersionUtils
import kotlinx.coroutines.flow.flowOf
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

    private val userPreferencesRepository = mock<UserPreferencesRepository>()

    private val versionUtils = mock<VersionUtils>()

    private lateinit var context : Context

    /**
     * Setup class.
     */
    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        whenever(userPreferencesRepository.getSystemDarkMode()).thenReturn(flowOf(false))
        whenever(userPreferencesRepository.getDarkMode()).thenReturn(flowOf(false))
        whenever(versionUtils.getAppVersion()).thenReturn("Version")
    }

    /**
     * Tests that the dark mode switch is disabled when the switch to use System dark mode is enabled.
     */
    @ExperimentalMaterialApi
    @Test
    fun testDarkModeDisabledWhenUseSystemDarkModeIsTrue() {
        val systemDarkModeSwitch = context.getString(R.string.system_dark_mode_switch)
        val darkModeSwitch = context.getString(R.string.dark_mode_switch)
        whenever(userPreferencesRepository.getSystemDarkMode()).thenReturn(flowOf(true))
        composeTestRule.setContent {
            SettingsScreen(userPreferencesRepository = userPreferencesRepository,
                navController = navController,
                versionUtils = versionUtils)
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
        val systemDarkModeSwitch = context.getString(R.string.system_dark_mode_switch)
        val darkModeSwitch = context.getString(R.string.dark_mode_switch)
        whenever(userPreferencesRepository.getSystemDarkMode()).thenReturn(flowOf(false))
        composeTestRule.setContent {
            SettingsScreen(userPreferencesRepository = userPreferencesRepository,
                navController = navController,
                versionUtils = versionUtils)
        }
        composeTestRule.onNodeWithContentDescription(systemDarkModeSwitch).assertIsEnabled()
        composeTestRule.onNodeWithContentDescription(darkModeSwitch).assertIsEnabled()
    }


    
}