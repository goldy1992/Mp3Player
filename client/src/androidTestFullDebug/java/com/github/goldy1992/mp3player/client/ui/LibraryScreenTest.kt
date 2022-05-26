package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.MockMediaBrowserAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import com.github.goldy1992.mp3player.client.ui.screens.library.SmallLibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.client.viewmodels.LibraryScreenViewModel
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
@HiltAndroidTest
class LibraryScreenTest {

    @Mock
    val mockMediaController = mock<MediaControllerAdapter>()

    @Mock
    val mockMediaBrowser = mock<MediaBrowserAdapter>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

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
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @ExperimentalPagerApi
    @Test
    fun testNavigationDrawerOpen() {

        val drawerState = DrawerState(DrawerValue.Closed)
        val navigationIconDescription = context.getString(R.string.navigation_drawer_menu_icon)
        composeTestRule.setContent {
            SmallLibraryScreen(
                navController = navController,
                pagerState = rememberPagerState(initialPage = 0),
              //  viewModel = hiltViewModel<LibraryScreenViewModel>(),
               viewModel  = LibraryScreenViewModel(MockMediaBrowserAdapter(MediaIdSubscriptionCallback(), MySearchCallback()), mockMediaController),
                bottomBar = {},
            drawerState = drawerState)
        }
        assertFalse(drawerState.isOpen)
        composeTestRule.onNodeWithContentDescription(navigationIconDescription).performClick()
        runBlocking {
            composeTestRule.awaitIdle()
            assertTrue(drawerState.isOpen)
        }
    }
}