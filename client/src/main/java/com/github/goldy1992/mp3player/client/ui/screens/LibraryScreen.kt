package com.github.goldy1992.mp3player.client.ui.screens

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.BOTTOM_BAR_SIZE
import com.github.goldy1992.mp3player.client.ui.NavigationDrawer
import com.github.goldy1992.mp3player.client.ui.PlayToolbar
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
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun LibraryScreen(navController: NavController,
                  scaffoldState: ScaffoldState = rememberScaffoldState(),
                  pagerState: PagerState = rememberPagerState(initialPage = 0),
                  viewModel: LibraryScreenViewModel = viewModel()
) {
    val rootItems: List<MediaItem> by viewModel.mediaBrowserAdapter.subscribeToRoot().observeAsState(emptyList())
    val navigationDrawerIconDescription = stringResource(id = R.string.navigation_drawer_menu_icon)
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Column {
                LibraryAppBar(scope, scaffoldState, navigationDrawerIconDescription, navController)
                LibraryTabs(pagerState = pagerState, rootItems = rootItems, scope = scope)
            }
        },
        bottomBar = {
            PlayToolbar(mediaController = viewModel.mediaControllerAdapter) {
                navController.navigate(Screen.NOW_PLAYING.name)
            }
        },
        drawerContent = {
            NavigationDrawer(navController = navController)
        },
        drawerShape =MaterialTheme.shapes.small,
        content = {
            LibraryScreenContent(
                    navController = navController,
                    pagerState = pagerState,
                    rootItems = rootItems,
                    viewModel = viewModel
                )
            })
}


@Composable
private fun LibraryAppBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navigationDrawerIconDescription: String,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(text = "Library")
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        if (scaffoldState.drawerState.isClosed) {
                            scaffoldState.drawerState.open()
                        }
                    }
                },
                modifier = Modifier.semantics {
                    contentDescription = navigationDrawerIconDescription
                })
            {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu Btn")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.SEARCH.name) }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            }
        })
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
            .background(MaterialTheme.colors.primary)) {
        Column(Modifier.weight(2f)) { }
        TabRow(
            modifier = Modifier.weight(6f),
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                    color = MaterialTheme.colors.secondary
                )
            }) {
            if (rootItemsLoaded(rootItems)) {
                rootItems.forEachIndexed { index, item ->
                    Tab(
                        content = {
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                                text = getRootMediaItemType(item = item)?.name ?: Constants.UNKNOWN,
                                style = MaterialTheme.typography.button
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
    pagerState: PagerState,
    rootItems: List<MediaItem>,
    mediaRepository: MediaRepository = MediaRepository(MutableLiveData()),
    viewModel: LibraryScreenViewModel = viewModel()
) {
    Row(
        Modifier
            .fillMaxSize()
            .padding(bottom = BOTTOM_BAR_SIZE) ) {
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