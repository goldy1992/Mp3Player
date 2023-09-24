package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.screens.library.SmallAppBar
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 *
 */
class NavigationDrawerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var context: Context




    /**
     * Setup method.
     */
    @Before
    fun setup() {
        this.context = InstrumentationRegistry.getInstrumentation().context
    }

    /**
     * Tests the appearance of [NavigationDrawer] when it is opened.
     */
    @Test
    fun testNavigationDrawerOpenAppearance() {
        val drawerState = DrawerState(DrawerValue.Open)
        val settingsDescription = context.getString(R.string.settings)
        composeTestRule.setContent {
            TestNavigationDrawerComposable(drawerState = drawerState)
        }

        composeTestRule.onNodeWithContentDescription(settingsDescription, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithContentDescription(settingsDescription, useUnmergedTree = true).assertIsDisplayed()
    }


    /**
     * Tests the [NavigationDrawer] exits when the drawer is closed but it not visible.
     */
    @Test
    fun testNavigationDrawerClosedAppearance() {
        val drawerState = DrawerState(DrawerValue.Closed)
        val settingsDescription = context.getString(R.string.settings)
        composeTestRule.setContent {
            TestNavigationDrawerComposable(drawerState = drawerState)
        }
        composeTestRule.onNodeWithContentDescription(settingsDescription, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithContentDescription(settingsDescription, useUnmergedTree = true).assertIsNotDisplayed()
    }


    /**
     * Tests the appearance of [NavigationDrawer] when it is opened.
     */
    @Test
    fun testNavigationClickOpenDrawer() {
        val drawerState = DrawerState(DrawerValue.Closed)
        val navigationIconDescription = context.getString(R.string.navigation_drawer_menu_icon)
        val settingsDescription = context.getString(R.string.settings)
        composeTestRule.setContent {
            TestNavigationDrawerComposable(drawerState)
        }


        composeTestRule.onNodeWithContentDescription(navigationIconDescription, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithContentDescription(navigationIconDescription, useUnmergedTree = true).performClick()

        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithContentDescription(settingsDescription).fetchSemanticsNodes().isNotEmpty()
        }
        assertTrue(drawerState.isOpen)
    }

    @Composable
    private fun TestNavigationDrawerComposable(drawerState: DrawerState) {
        val scope = rememberCoroutineScope()
        Column(Modifier.fillMaxSize()) {
            ModalNavigationDrawer(
                drawerContent = {
                    NavigationDrawerContent(
                        currentScreen = Screen.LIBRARY
                    )
                },
                drawerState = drawerState
            )
            {
                Scaffold(
                    bottomBar = {},
                    topBar = {
                        SmallAppBar(
                            title = "libraryText",
                            onClickNavIcon = {
                                scope.launch {
                                    if (drawerState.isOpen) {
                                        drawerState.close()
                                    } else {
                                        drawerState.open()
                                    }
                                }
                            },
                            onClickSearchIcon = {}
                        )
                    },
                ) {
                    Column(Modifier.padding(it)) {

                    }
                }
            }

        }
    }
}