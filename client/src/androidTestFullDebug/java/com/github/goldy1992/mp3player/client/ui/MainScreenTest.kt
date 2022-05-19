package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.material.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Test class for the [MainScreen] composable function.
 */
class MainScreenTest {

    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    @Mock
    val mockMediaBrowser = mock<MediaBrowserAdapter>()

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mediaRepository = MediaRepository(MutableLiveData(listOf(MediaItemUtils.getEmptyMediaItem())))

    private val navController = mock<NavController>()

    private lateinit var context : Context

    /**
     * Setup method.
     */
    @Before
    fun setup() {
        this.context = InstrumentationRegistry.getInstrumentation().context
        whenever(mockMediaController.isPlaying).thenReturn(MutableLiveData(true))
    }

    /**
     * Tests the [NavigationDrawer] is opened when the Navigation menu icon is clicked.
     */
    @ExperimentalPagerApi
    @ExperimentalMaterialApi
    @Test
    fun testNavigationDrawerOpen() {

        val scaffoldState = ScaffoldState(drawerState = DrawerState(DrawerValue.Closed),
            snackbarHostState = SnackbarHostState()
        )
        val navigationIconDescription = context.getString(R.string.navigation_drawer_menu_icon)
        composeTestRule.setContent {
            MainScreen(
                navController = navController,
                //mediaRepository = mediaRepository,
                mediaBrowserAdapter = mockMediaBrowser,
                windowSize = getWindowSizeClass(DpSize(400.dp, 400.dp)),
                mediaController = mockMediaController,
                scaffoldState = scaffoldState)
        }
        assertFalse(scaffoldState.drawerState.isOpen)
        composeTestRule.onNodeWithContentDescription(navigationIconDescription).performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            assertTrue(scaffoldState.drawerState.isOpen)
        }
    }
}