package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.library.SmallLibraryAppBar
import com.github.goldy1992.mp3player.client.ui.screens.library.SmallLibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock

/**
 * Test class for the [MainScreen] composable function.
 */
@HiltAndroidTest
class LibraryScreenTest {


    private val testMediaRepository = TestMediaRepository()
//    val onChildrenChangedFlowObj = mock<OnChildrenChangedFlow>()
//    val onChildrenChangedFlow = MutableStateFlow(
//                                    OnChildrenChangedEventHolder(mockMediaBrowser,
//                                        "",
//                                        1,
//                                        MediaLibraryService.LibraryParams.Builder().build())
//    )


    @Mock
    val mainDispatcher : CoroutineDispatcher = mock<CoroutineDispatcher>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()


    private val navController = mock<NavController>()

    lateinit var context : Context

    private lateinit var libraryScreenViewModel: LibraryScreenViewModel

    /**
     * Setup method.
     */
    @Before
    fun setup() {
        this.context = InstrumentationRegistry.getInstrumentation().context
        this.libraryScreenViewModel = LibraryScreenViewModel(
            mediaRepository = testMediaRepository
        )
    }

    /**
     * Tests the [NavigationDrawer] is opened when the Navigation menu icon is clicked.
     */
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @ExperimentalPagerApi
    @Test
    fun testNavigationDrawerOpen() {
        val drawerState = DrawerState(DrawerValue.Closed)
        val navigationIconDescription = context.getString(R.string.navigation_drawer_menu_icon)
        composeTestRule.setContent {
            val scope = rememberCoroutineScope()
            SmallLibraryScreen(
                bottomBar = {},
                topBar = {          SmallLibraryAppBar(
                    title = "libraryText",
                    onClickNavIcon = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    },
                    onClickSearchIcon = {}
                )
                },
                navDrawerContent= {
                    NavigationDrawerContent(
                        navController = navController,
                        currentScreen = Screen.LIBRARY
                    )
                },
                drawerState = drawerState) {

            }
        }
        assertFalse(drawerState.isOpen)
        composeTestRule.onNodeWithContentDescription(navigationIconDescription).performClick()
        assertTrue(drawerState.isOpen)
    }
}