package com.github.goldy1992.mp3player.client.ui.screens.library

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.*
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderList
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.viewmodels.LibraryScreenViewModel
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getRootMediaItemType
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The Main Screen of the app.
 *
 * @param navController The [NavController].
 * @param scaffoldState The [ScaffoldState]. Optional, if not provided one will be created.
 * @param pagerState The [PagerState]. Optional, if not provided one will be created using the [MediaRepository.rootItems].
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun LibraryScreen(navController: NavController,
                  pagerState: PagerState = rememberPagerState(initialPage = 0),
                  viewModel: LibraryScreenViewModel = viewModel(),
                  windowsSize: WindowSize = WindowSize.Compact
) {

    val drawerState : DrawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val rootItems: List<MediaItem> by viewModel.mediaBrowserAdapter.subscribeToRoot().observeAsState(emptyList())
    val scope = rememberCoroutineScope()
    val isLargeScreen = windowsSize == WindowSize.Expanded

    val topBar : @Composable () -> Unit  = {
        LibraryAppBar(scope, navController) {
            if (drawerState.isClosed) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }
    }

    val bottomBar : @Composable () -> Unit = {
        PlayToolbar(mediaController = viewModel.mediaControllerAdapter) {
            navController.navigate(Screen.NOW_PLAYING.name)
        }
    }

    val content : @Composable (PaddingValues) -> Unit = {
        LibraryScreenContent(
            scope = scope,
            navController = navController,
            pagerState = pagerState,
            rootItems = rootItems,
            viewModel = viewModel
        )
    }

    if (isLargeScreen) {
        LargeLibraryScreen(
            drawerState = drawerState,
            viewModel = viewModel,
            navController = navController,
            rootItems = rootItems,
            pagerState = pagerState
        )
    } else {
        SmallLibraryScreen(topBar = topBar,
            drawerState = drawerState,
            bottomBar = bottomBar,
            drawerContent = { NavigationDrawer(navController = navController) }) {
        }
    }
}

/**
 * The large Library Screen.
 *
 * @param scaffoldState The [ScaffoldState].
 * @param topBar The Top Bar.
 * @param bottomBar The Bottom Bar.
 * @param navigationColumn The Navigation Column.
 * @param content The content of the Library Screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun LargeLibraryScreen(
    drawerState : DrawerState,
    viewModel: LibraryScreenViewModel,
    scope: CoroutineScope = rememberCoroutineScope(),
    pagerState: PagerState = rememberPagerState(),
    rootItems: List<MediaItem> = emptyList(),
    navController: NavController = rememberNavController()) {


    // TODO: Replace with DismissibleNavigationDrawer when better support for content resizing.
    PermanentNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerContainerColor = MaterialTheme.colorScheme.surfaceTint,
        drawerContent = {
            DismissibleNavigationDrawerContent(
                navController = navController
            ) },
//        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = {
                LibraryAppBar(scope, navController) {
                    if (drawerState.isClosed) {
                        drawerState.open()
                    } else {
                        drawerState.close()
                    }
                }

            },
            bottomBar = {
                PlayToolbar(mediaController = viewModel.mediaControllerAdapter) {
                    navController.navigate(Screen.NOW_PLAYING.name)
                }

            }
            ) {
            LibraryScreenContent(
                scope = scope,
                navController = navController,
                pagerState = pagerState,
                rootItems = rootItems,
                viewModel = viewModel
            )

        }
        }
}

/**
 * The large Library Screen.
 *
 * @param scaffoldState The [ScaffoldState].
 * @param topBar The Top Bar.
 * @param bottomBar The Bottom Bar.
 * @param navigationColumn The Navigation Column.
 * @param content The content of the Library Screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun SmallLibraryScreen(
    drawerState : DrawerState = rememberDrawerState(DrawerValue.Closed),
    topBar : @Composable () -> Unit,
    bottomBar : @Composable () -> Unit,
    drawerContent : @Composable (ColumnScope.() -> Unit),
    content : @Composable (PaddingValues) -> Unit) {
    ModalNavigationDrawer(drawerContent = drawerContent,
    drawerState = drawerState) {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            content = content
        )

    }
}




/**
 * Util method to check if the Root items are loaded.
 */
private fun rootItemsLoaded(items : List<MediaItem>) : Boolean {
    return !(items.isEmpty() || MediaItemUtils.getMediaId(items.first()) == Constants.EMPTY_MEDIA_ITEM_ID)
}

/**
 * Util method to return the String of the [MediaItemType].
 * @param mediaItemType The [MediaItemType] of which to get the String.
 */
@Composable
fun getRootItemText(mediaItemType: MediaItemType): String {
    return when (mediaItemType) {
        MediaItemType.SONGS -> stringResource(id = R.string.songs)
        MediaItemType.FOLDERS -> stringResource(id = R.string.folders)
        else -> "" // TOOO: Add a return for an unknown MediaItemType
    }
}

@ExperimentalPagerApi
@Composable
private fun LibraryTabs(
    pagerState: PagerState,
    rootItems: List<MediaItem>,
    scope: CoroutineScope
) {
    Row(
        Modifier
            .fillMaxWidth()
    //        .background(MaterialTheme.colors.primary)
    ) {
        Column(Modifier.weight(2f)) { }
        TabRow(
            modifier = Modifier.weight(6f),
            selectedTabIndex = pagerState.currentPage,
//            indicator = { tabPositions ->
//                TabRowDefaults.Indicator(
//                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
//                   // color = MaterialTheme.colors.secondary
//                )
    //        }
    ) {
            if (rootItemsLoaded(rootItems)) {
                rootItems.forEachIndexed { index, item ->
                    Tab(
                        content = {
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                                text = getRootMediaItemType(item = item)?.name ?: Constants.UNKNOWN,
                            )
                        },
                        onClick = {                    // Animate to the selected page when clicked
                            scope.launch {
                                Log.i(
                                    "MainScreen",
                                    "Clicked to go to index ${index}, string: ${item.mediaId} "
                                )
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        selected = pagerState.currentPage == index
                    )
                }
            } else {
                Tab(
                    content = { CircularProgressIndicator() },
                    onClick = { },
                    selected = pagerState.currentPage == 0
                )
            }
        }
        Column(Modifier.weight(2f)) { }
    }
}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun LibraryScreenContent(
    navController: NavController,
    scope: CoroutineScope = rememberCoroutineScope(),
    pagerState: PagerState,
    rootItems: List<MediaItem>,
    mediaRepository: MediaRepository = MediaRepository(MutableLiveData()),
    viewModel: LibraryScreenViewModel = viewModel()
) {
    Column(Modifier.fillMaxHeight()) {
        LibraryTabs(
            pagerState = pagerState,
            rootItems = rootItems,
            scope = scope
        )

        Row(
            Modifier
                .fillMaxSize()
                .padding(bottom = BOTTOM_BAR_SIZE)
        ) {
            if (rootItems.isEmpty()) {
                LoadingIndicator()
            } else {
                TabBarPages(
                    navController = navController,
                    pagerState = pagerState,
                    rootItems = rootItems,
                    viewModel = viewModel
                )
            }
        }
    }

}


/**
 * Displays the pages for each of the Home bar tabs.
 * @param navController The [NavController].
 * @param pagerState The [PagerState] of the Tab Bar.
 * @param rootItems The [List] of [MediaItem]s to display on the Tab Bar.
 * @param modifier The [Modifier].
 * @param viewModel The [LibraryScreenViewModel].
 */
@ExperimentalPagerApi
@Composable
fun TabBarPages(navController: NavController,
                pagerState: PagerState,
                rootItems: List<MediaItem>,
                modifier: Modifier = Modifier,
                viewModel : LibraryScreenViewModel = viewModel()
) {
    Column(
        modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(),
            count = rootItems.size
        ) { pageIndex ->
            val currentItem = rootItems[pageIndex]
            val children = viewModel.mediaBrowserAdapter.subscribe(MediaItemUtils.getMediaId(currentItem)!!)

            if (children == null) {
                CircularProgressIndicator()
            } else {
                when (getRootMediaItemType(currentItem)) {
                    MediaItemType.SONGS -> {
                        SongList(
                            songs = children,
                            mediaControllerAdapter = viewModel.mediaControllerAdapter
                        ) {
                            val libraryId = MediaItemUtils.getLibraryId(it)
                            Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                            viewModel.mediaControllerAdapter.playFromMediaId(libraryId, null)
                        }
                    }
                    MediaItemType.FOLDERS -> {
                        FolderList(foldersData = children!!) {
                            //viewModel.currentFolder = it
                            navController.navigate(Screen.FOLDER.name)
                        }
                    }
                    else -> {
                        Log.i("mainScreen", "unrecognised Media Item")
                    }
                }
            }
        }
    }
}