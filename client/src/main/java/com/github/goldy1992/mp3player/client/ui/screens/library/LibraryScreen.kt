@file:OptIn(ExperimentalAnimationApi::class)

package com.github.goldy1992.mp3player.client.ui.screens.library

import android.net.Uri
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.*
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderList
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.viewmodels.LibraryScreenViewModel
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getRootMediaItemType
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isEmpty
import java.util.*
import kotlin.collections.HashMap

private const val logTag = "LibraryScreen"
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
fun LibraryScreen(navController: NavController = rememberAnimatedNavController(),
                  pagerState: PagerState = rememberPagerState(initialPage = 0),
                  viewModel: LibraryScreenViewModel = viewModel(),
                  windowSize: WindowSize = WindowSize.Compact,
                  scope: CoroutineScope = rememberCoroutineScope()
) {
    val rootItems by viewModel.rootItems.collectAsState()
    val rootItemsMap by viewModel.rootItemMap.collectAsState()
    val isPlaying by viewModel.isPlaying.state.collectAsState()

    val onSongSelected : (Int, List<MediaItem>) -> Unit =  {
            itemIndex, mediaItemList ->
        viewModel.mediaControllerAdapter.playFromSongList(itemIndex, mediaItemList)
    }
    val onFolderSelected : (MediaItem) -> Unit = {

        val folderId = it.mediaId
        val encodedFolderLibraryId = Uri.encode(folderId)
        val directoryPath = MediaItemUtils.getDirectoryPath(it)
        val encodedFolderPath = Uri.encode(directoryPath)
        val folderName = MediaItemUtils.getDirectoryName(it)
        navController.navigate(
            Screen.FOLDER.name
                    + "/" + encodedFolderLibraryId
                    + "/" + folderName
                    + "/" + encodedFolderPath)
    }

    val navDrawerContent : @Composable () -> Unit = {
        NavigationDrawerContent(
            navController = navController,
            currentScreen = Screen.LIBRARY
        )
    }

    val m : EnumMap<MediaItemType, Any> = EnumMap(MediaItemType::class.java)
    m[MediaItemType.SONGS] = onSongSelected
    m[MediaItemType.FOLDERS] = onFolderSelected

    val isLargeScreen = windowSize == WindowSize.Expanded

    val bottomBar : @Composable () -> Unit = {
        PlayToolbar(isPlaying= { isPlaying },
            mediaController = viewModel.mediaControllerAdapter,
            navController = navController,
            scope = scope)
    }
    val topBar : @Composable () -> Unit =
        if (isLargeScreen) {
            {
                LargeAppBar(title = "Library") {
                    scope.launch {
                        navController.navigate(Screen.SEARCH.name)
                    }
                }
            }
        } else {
            {
                val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                SmallLibraryAppBar(
                    onClickNavIcon = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    },
                    onClickSearchIcon = {
                        navController.navigate(Screen.SEARCH.name)
                    }
                )
            }
        }

    if (isLargeScreen) {
        LargeLibraryScreen(
            scope = scope,
            navDrawerContent = navDrawerContent,
            rootItems = rootItems,
            pagerState = pagerState
        )
    } else {
        SmallLibraryScreen(
            navController = navController,
            scope = scope,
            bottomBar = bottomBar,
            rootItems = {rootItems},
            pagerState = pagerState)
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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun LargeLibraryScreen(
    scope: CoroutineScope = rememberCoroutineScope(),
    pagerState: PagerState = rememberPagerState(),
    navDrawerContent : @Composable () -> Unit = {},
    bottomBar : @Composable () -> Unit,
    topBar : @Composable () -> Unit,
    rootItemsProvider: () -> List<MediaItem>,
    onItemSelectedMapProvider : () -> EnumMap<MediaItemType, Any > = { EnumMap(MediaItemType::class.java) },
    rootItemsMapProvider : () -> HashMap<String, List<MediaItem>> = { HashMap() },
    currentMediaItemProvider : () -> MediaItem = {MediaItem.EMPTY},
    isPlayingProvider : () -> Boolean = {false}
) {


    // TODO: Replace with DismissibleNavigationDrawer when better support for content resizing.
    PermanentNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerContent = navDrawerContent,
    ) {
        val context = LocalContext.current
        val libraryText = context.getString(R.string.library)
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar
        ) {
            Surface(Modifier.width(500.dp)) {
                LibraryScreenContent(
                    scope = scope,
                    pagerState = pagerState,
                    rootItemsProvider =  rootItemsProvider,
                    onItemSelectedMapProvider = onItemSelectedMapProvider,
                    rootItemsMapProvider = rootItemsMapProvider,
                    currentMediaItemProvider = currentMediaItemProvider,
                    isPlayingProvider = isPlayingProvider,
                    modifier = Modifier.padding(it)
                )
            }
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
    navController: NavController = rememberAnimatedNavController(),
    scope : CoroutineScope = rememberCoroutineScope(),
    bottomBar : @Composable () -> Unit,
    pagerState: PagerState = rememberPagerState(),
    rootItems: () -> List<MediaItem> = {emptyList() },

) {

   ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                navController = navController
            )
        },
        drawerState = drawerState) {
        Scaffold(
            topBar = {
                SmallLibraryAppBar(scope, navController) {
                    if (drawerState.isClosed) {
                        drawerState.open()
                    } else {
                        drawerState.close()
                    }
                }
            },
            bottomBar = bottomBar,

        ) {
            LibraryScreenContent(
                scope = scope,
                navController = navController,
                pagerState = pagerState,
                mediaItemMap = {rootItemMap},
                viewModel = viewModel,
                modifier = Modifier.padding(it)
            )

        }

    }
}

@ExperimentalPagerApi
@Composable
private fun LibraryTabs(
    pagerState: PagerState,
    rootItemsProvider:  () -> List<MediaItem>,
    scope: CoroutineScope
) {

    val rootItems = rootItemsProvider()

    Column {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            rootItems.forEachIndexed { index, item ->
                val isSelected = index == pagerState.currentPage
                Tab(
                    selected = isSelected,
                    modifier = Modifier.height(48.dp),
                    content = {
                        Text(
                            text = getRootMediaItemType(item = item)?.name ?: Constants.UNKNOWN,
                            style = androidx.compose.material.MaterialTheme.typography.button,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
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
                )
            }
        }
    }
}


@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun LibraryScreenContent(
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    pagerState: PagerState,
    rootItemsProvider: () -> List<MediaItem>,
    onItemSelectedMapProvider : () -> EnumMap<MediaItemType, Any > = { EnumMap(MediaItemType::class.java) },
    rootItemsMapProvider : () -> HashMap<String, List<MediaItem>> = { HashMap() },
    currentMediaItemProvider : () -> MediaItem = {MediaItem.EMPTY},
    isPlayingProvider : () -> Boolean = {false}

) {

    Column(modifier.fillMaxHeight()) {
        LibraryTabs(
            pagerState = pagerState,
            rootItemsProvider = rootItemsProvider,
            scope = scope
        )

        Row(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
            TabBarPages(
                pagerState = pagerState,
                rootItemsMapProvider = rootItemsMapProvider,
                currentMediaItemProvider = currentMediaItemProvider,
                onItemSelectedMapProvider = onItemSelectedMapProvider,
                isPlayingProvider = isPlayingProvider
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
@OptIn(ExperimentalCoilApi::class)
@ExperimentalPagerApi
@Composable
fun TabBarPages(
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    onItemSelectedMapProvider : () -> EnumMap<MediaItemType, Any > = { EnumMap(MediaItemType::class.java) },
    rootItemsMapProvider : () -> HashMap<String, List<MediaItem>> = { HashMap() },
    currentMediaItemProvider : () -> MediaItem = {MediaItem.EMPTY},
    isPlayingProvider : () -> Boolean = {false}

) {
    val onItemSelectedMap = onItemSelectedMapProvider()
    val rootItemMap = rootItemsMapProvider()
    val currentItem = currentMediaItemProvider()
    val mapKeys = rootItemMap.keys.toList()
    Column(
        modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            count = rootItemMap.size
        ) { pageIndex ->
            val children = rootItemMap[mapKeys[pageIndex]] ?: emptyList()
            if (isEmpty(children)) {
                Column(modifier = Modifier.fillMaxSize(),
                       horizontalAlignment = Alignment.CenterHorizontally,
                       verticalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                }

            } else {
                when (getRootMediaItemType(currentItem)) {
                    MediaItemType.SONGS -> {
                        SongList(
                            songs = children,
                            isPlayingProvider = isPlayingProvider,
                            currentMediaItemProvider = currentMediaItemProvider
                        ) { itemIndex, mediaItemList ->
                            val callable = onItemSelectedMap[MediaItemType.SONGS] as? (Int, List<MediaItem>) -> Unit
                            if (callable != null) {
                                callable(itemIndex, mediaItemList)
                            }
                            Log.i("ON_CLICK_SONG", "clicked song with id : ${mediaItemList[itemIndex].mediaId}")
                        }
                        Log.i(logTag, "last song name: ${children.last().mediaMetadata.title}")
                    }
                    MediaItemType.FOLDERS -> {
                        FolderList(folders = children) {

                            val callable =
                                onItemSelectedMap[MediaItemType.SONGS] as? (MediaItem) -> Unit
                            if (callable != null) {
                                callable(it)
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeAppBar(title : String,
                onSearchIconClicked : () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        actions = {
            IconButton(onClick = onSearchIconClicked) {
                Icon(imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
    )
}