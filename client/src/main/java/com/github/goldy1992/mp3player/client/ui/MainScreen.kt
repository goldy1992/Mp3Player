package com.github.goldy1992.mp3player.client.ui

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.ui.lists.folder.SongsInFolderList
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getEmptyMediaItem
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getRootMediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.isEmptyMediaItem
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The Main Screen of the app.
 *
 * @param navController The [NavController].
 * @param mediaController The [MediaControllerAdapter].
 * @param mediaRepository The [MediaRepository].
 * @param scaffoldState The [ScaffoldState]. Optional, if not provided one will be created.
 * @param pagerState The [PagerState]. Optional, if not provided one will be created using the [MediaRepository.rootItems].
 */
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun MainScreen(navController: NavController,
               windowSize: WindowSize,
               mediaRepository: MediaRepository,
               mediaController: MediaControllerAdapter,
               mediaBrowserAdapter : MediaBrowserAdapter,
               scaffoldState: ScaffoldState = rememberScaffoldState(),
               pagerState: PagerState = rememberPagerState(initialPage = 0)
) {
    val isExpanded = windowSize == WindowSize.Expanded
    val rootItems: List<MediaItem> by mediaRepository.rootItems.observeAsState(emptyList())
    val scope = rememberCoroutineScope()

    CustomScaffold(            scaffoldState = scaffoldState,
        navController = navController,
        scope = scope,
        mediaController = mediaController,
        extendTopAppBar = {
            if (!isExpanded) {
                MainTabs(pagerState = pagerState, rootItems = rootItems, scope = scope)
            }
        },
        content = {
            if (isExpanded) {
                LargeMainScreenContent(
                    mediaBrowser = mediaBrowserAdapter,
                    scope = scope,
                    rootItems = rootItems,
                    mediaController = mediaController,
                    mediaRepository = mediaRepository
                )
                // create large main screen
            } else {
                SmallMainScreenContent(
                    navController = navController,
                    pagerState = pagerState,
                    rootItems = rootItems,
                    mediaController = mediaController,
                    mediaRepository = mediaRepository
                )
            }
        }
    )


}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
private fun SmallMainScreenContent(
    navController: NavController,
    pagerState: PagerState,
    rootItems: List<MediaItem>,
    mediaController: MediaControllerAdapter,
    mediaRepository: MediaRepository
) {
    Row(
            Modifier
                    .fillMaxSize()
                    .padding(bottom = BOTTOM_BAR_SIZE) ) {
            if (rootItems.isEmpty() ) {
                LoadingIndicator()
            } else {
                TabBarPages(
                    navController = navController,
                    mediaRepository = mediaRepository,
                    mediaController = mediaController,
                    pagerState = pagerState,
                    rootItems = rootItems
                )
        }
    }

}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
private fun LargeMainScreenContent(
    mediaBrowser : MediaBrowserAdapter,
    scope: CoroutineScope,
    rootItems: List<MediaItem>,
    mediaController: MediaControllerAdapter,
    mediaRepository: MediaRepository
) {
    val mediaItemSelected = remember { mutableStateOf(getEmptyMediaItem()) }

    if (rootItems.isEmpty()) {
        LoadingIndicator()
    } else {

        val navigationItemSelected = remember { mutableStateOf(getRootMediaItemType(rootItems[0]))}
        Column(
           modifier = Modifier
                   .fillMaxSize()
                   .padding(bottom = BOTTOM_BAR_SIZE)
        )
        {
            Divider(thickness = 5.dp, color = MaterialTheme.colors.background)

            Row(Modifier.fillMaxSize()) {
                NavigationRail(Modifier.align(Alignment.CenterVertically)) {
                    if (rootItems.isEmpty()) {
                        LoadingIndicator()
                    } else {
                        Column(verticalArrangement =  Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                            rootItems.forEach() {
                                NavigationRailItem(
                                    selected = navigationItemSelected.value == getRootMediaItemType(it),
                                    onClick = {
                                        scope.launch {
                                            val selectedItem = getRootMediaItemType(it)
                                            if (navigationItemSelected.value != selectedItem) {
                                                navigationItemSelected.value = getRootMediaItemType(it)
                                                mediaItemSelected.value = getEmptyMediaItem()
                                            }
                                        }
                                    },
                                    label = { Text(MediaItemUtils.getTitle(it)) },
                                    icon = {
                                        when(getRootMediaItemType(it)) {
                                            MediaItemType.SONGS -> Icon(Icons.Filled.MusicNote, "")
                                            MediaItemType.FOLDERS -> Icon(Icons.Filled.Folder, "")
                                        }
                                     },
                                )
                            }
                        }
                    }

                }
                Column(Modifier.weight(0.5f)) {
                    when(navigationItemSelected.value) {
                        MediaItemType.SONGS -> {
                            val songs = mediaRepository.itemMap[MediaItemType.SONGS]
                            if (songs == null) {
                                CircularProgressIndicator()
                            } else {
                                SongList(songsData = songs) {
                                    val libraryId = MediaItemUtils.getLibraryId(it)
                                    Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                                    mediaController.playFromMediaId(libraryId, null)
                                    mediaItemSelected.value = it
                                }
                            }
                        }
                        MediaItemType.FOLDERS -> {
                            val folders = mediaRepository.itemMap[MediaItemType.FOLDERS]
                            if (folders == null) {
                                CircularProgressIndicator()
                            } else {
                                FolderList(foldersData = folders) {
                                    mediaRepository.currentFolder = it
                                    mediaItemSelected.value = it!!
                                }
                            }
                        }
                    }

                }
                Column(Modifier.weight(0.5f)) {
                    if (isEmptyMediaItem(mediaItemSelected.value)) {
                        Text(text = "Select a media item")
                    }
                    val currentItem = mediaItemSelected.value
                    when (MediaItemUtils.getMediaItemType(currentItem)) {
                        MediaItemType.SONG -> Text(text = "Selected ${MediaItemUtils.getTitle(currentItem)}")
                        MediaItemType.FOLDER -> {
                            val folderItems = remember(currentItem.mediaId) {mediaBrowser.subscribe(MediaItemUtils.getLibraryId(currentItem)!!) }
                            SongsInFolderList(folder = currentItem, songsInFolders = folderItems) {
                                val libraryId = MediaItemUtils.getLibraryId(it)
                                Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                                mediaController.playFromMediaId(libraryId, null)
                            } }
                        else -> Text(text = "selected ${Constants.UNKNOWN}")
                    }
                }
            }
        }
    }

}

/**
 * Displays the pages for each of the Home bar tabs.
 * @param navController The [NavController].
 * @param mediaController The [MediaControllerAdapter].
 * @param mediaRepository The [MediaRepository].
 * @param pagerState The [PagerState] of the Tab Bar.
 * @param rootItems The [List] of [MediaItem]s to display on the Tab Bar.
 */
@ExperimentalPagerApi
@Composable
fun TabBarPages(navController: NavController,
                mediaRepository: MediaRepository,
                mediaController: MediaControllerAdapter,
                pagerState: PagerState,
                rootItems: List<MediaItem>,
                modifier: Modifier = Modifier) {
    Column(
       modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(),
            count = mediaRepository.rootItems.value?.size ?: 0
            ) { pageIndex ->

            val currentItem = rootItems[pageIndex]

            when (getRootMediaItemType(currentItem)) {
                MediaItemType.SONGS -> {
                    val songs = mediaRepository.itemMap[MediaItemType.SONGS]
                    if (songs == null) {
                        CircularProgressIndicator()
                    } else {
                        SongList(songsData = songs) {
                            val libraryId = MediaItemUtils.getLibraryId(it)
                            Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                            mediaController.playFromMediaId(libraryId, null)
                        }                 }
                }
                MediaItemType.FOLDERS -> {
                    val folders = mediaRepository.itemMap[MediaItemType.FOLDERS]
                    if (folders == null) {
                        CircularProgressIndicator()
                    } else {
                        FolderList(foldersData = folders) {
                            mediaRepository.currentFolder = it
                            navController.navigate(Screen.FOLDER.name)
                        }
                    }
                }
                else -> {
                    Log.i("mainScreen", "unrecognised Media Item")
                }
            }
        }
    }
}


/**
 * Contructs the AppBar on the HomeScreen
 * @param navController The [NavController].
 * @param pagerState The [PagerState] of the Tab Bar.
 * @param scope The [CoroutineScope].
 * @param scaffoldState The [ScaffoldState].
 * @param rootItems The [List] of [MediaItem]s to display on the Tab Bar.
 */
@ExperimentalPagerApi
@Composable
fun HomeAppBar(
    navController: NavController,
    scope : CoroutineScope,
    scaffoldState: ScaffoldState) {
    val navigationDrawerIconDescription = stringResource(id = R.string.navigation_drawer_menu_icon)
        TopAppBar(
            title = {
                Text(text = "MP3 Player")
            },
            backgroundColor = MaterialTheme.colors.primary,
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
            },
            contentColor = MaterialTheme.colors.onPrimary,
        )
} // HomeAppBar

@ExperimentalPagerApi
@Composable
private fun MainTabs(
    pagerState: PagerState,
    rootItems: List<MediaItem>,
    scope: CoroutineScope
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        if (rootItemsLoaded(rootItems)) {
            rootItems.forEachIndexed { index, item ->
                Tab(
                    content = {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                            text = getRootItemText(mediaItemType = getRootMediaItemType(item = item)!!),
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

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
private fun CustomScaffold(
    scaffoldState: ScaffoldState,
    navController: NavController,
    scope: CoroutineScope,
    mediaController: MediaControllerAdapter,
    extendTopAppBar: @Composable () -> Unit = {},
    content : @Composable (PaddingValues) -> Unit = {}
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                HomeAppBar(
                    navController = navController,
                    scope = scope,
                    scaffoldState = scaffoldState,
                )
                extendTopAppBar
            }
        },
        bottomBar = {
            PlayToolbar(mediaController = mediaController) {
                navController.navigate(Screen.NOW_PLAYING.name)
            }
        },
        drawerContent = {
            NavigationDrawer(navController = navController)
        },
        content = content)
}