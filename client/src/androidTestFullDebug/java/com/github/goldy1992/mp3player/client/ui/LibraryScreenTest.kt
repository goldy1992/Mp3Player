package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.media3.session.MediaLibraryService
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.data.flows.mediabrowser.OnChildrenChangedFlow
import com.github.goldy1992.mp3player.client.ui.screens.library.SmallLibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.client.viewmodels.LibraryScreenViewModel
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
class LibraryScreenTest : MediaTestBase() {


    val onChildrenChangedFlowObj = mock<OnChildrenChangedFlow>()
    val onChildrenChangedFlow = MutableStateFlow(
                                    OnChildrenChangedEventHolder(mockMediaBrowser,
                                        "",
                                        1,
                                        MediaLibraryService.LibraryParams.Builder().build())
    )


    @Mock
    val mainDispatcher : CoroutineDispatcher = mock<CoroutineDispatcher>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mediaRepository = MediaRepository(MutableLiveData(listOf(MediaItemUtils.getEmptyMediaItem())))

    private val navController = mock<NavController>()

    override lateinit var context : Context

    private lateinit var libraryScreenViewModel: LibraryScreenViewModel

    /**
     * Setup method.
     */
    @Before
    override fun setup() {
        val scope : CoroutineScope
        val mainDispatcher = Dispatchers.Main
        runBlocking {
            scope = this
        }
        super.setup(scope, mainDispatcher)
        this.context = InstrumentationRegistry.getInstrumentation().context
        whenever(onChildrenChangedFlowObj.flow).thenReturn(onChildrenChangedFlow)

        this.libraryScreenViewModel = LibraryScreenViewModel(
            mediaBrowserAdapter = mediaBrowserAdapter,
            onChildrenChangedFlow = onChildrenChangedFlowObj,
            mediaControllerAdapter = mediaControllerAdapter,
            metadataFlow = metadataFlowObj,
            isPlayingFlow = isPlayingFlowObj,
            mainDispatcher = mainDispatcher)
//        whenever(isPlayingFLow).thenReturn(MutableStateFlow(true))
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
                viewModel  = libraryScreenViewModel,
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