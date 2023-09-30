package com.github.goldy1992.mp3player.client.ui.screens.library

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.*
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.Albums
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.Folders
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.ui.*
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.navigation.AppNavigationRail
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContentInternal
import com.github.goldy1992.mp3player.client.ui.lists.albums.AlbumsList
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderList
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.utils.NavigationUtils
import com.github.goldy1992.mp3player.client.utils.NavigationUtils.toggleNavigationDrawer
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

private const val LOG_TAG = "LibraryScreen"
/**
 * The Main Screen of the app.
 *
 * @param navController The [NavController].
 * @param pagerState The [PagerState].
 * @param viewModel The [LibraryScreenViewModel].
 */
@OptIn(
   ExperimentalMaterial3Api::class, ExperimentalCoilApi::class
)
@Composable
fun LibraryScreen(
    navController: NavController = rememberNavController(),
    viewModel: LibraryScreenViewModel = viewModel(),
    windowSize: WindowSizeClass = DEFAULT_WINDOW_CLASS_SIZE,
    scope: CoroutineScope = rememberCoroutineScope()
) {

    val rootItems by viewModel.root.collectAsState()
    val songs by viewModel.songs.collectAsState()
    val folders by viewModel.folders.collectAsState()
    val albums by viewModel.albums.collectAsState()

    val onSongSelected : (Int, Playlist) -> Unit =  {
            itemIndex, mediaItemList ->
        viewModel.playPlaylist(mediaItemList, itemIndex)
    }
    val onFolderSelected : (Folder) -> Unit = { NavigationUtils.navigate(navController, it) }
    val onAlbumSelected : (Album) -> Unit = { NavigationUtils.navigate(navController, it) }

    val onItemSelectedMap : EnumMap<MediaItemType, Any> = EnumMap(MediaItemType::class.java)
    onItemSelectedMap[MediaItemType.SONGS] = onSongSelected
    onItemSelectedMap[MediaItemType.FOLDERS] = onFolderSelected
    onItemSelectedMap[MediaItemType.ALBUMS] = onAlbumSelected


    val isPlaying by viewModel.isPlaying.state().collectAsState()
    val currentMediaItem by viewModel.currentSong.state().collectAsState()
    val drawerState : DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    Navigation(
        navController = navController,
        windowSizeClass = windowSize,
        drawerState = drawerState,
        scope = scope
    ) {

        val scrollBehavior : TopAppBarScrollBehavior =
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            topBar = {
                LibraryAppBar(
                    windowSize = windowSize,
                    scrollBehavior = scrollBehavior,
                    onClickNavIcon = { scope.launch { toggleNavigationDrawer(drawerState)} },
                    onClickSearchIcon = { navController.navigate(Screen.SEARCH.name) }
                )
            }
            ,
            bottomBar = {
                PlayToolbar(
                    isPlayingProvider = { isPlaying },
                    onClickPlay = { viewModel.play()
                        Log.v(LOG_TAG, "PlayToolbar.onClickPlay() clicked play")},
                    onClickPause = {viewModel.pause()
                        Log.v(LOG_TAG, "PlayToolbar.onClickPause() clicked pause")},
                    onClickSkipPrevious = { viewModel.skipToPrevious() },
                    onClickSkipNext = { viewModel.skipToNext() },
                    onClickBar = { navController.navigate(Screen.NOW_PLAYING.name)},
                    currentSongProvider = { currentMediaItem },
                    windowSizeClass = windowSize
                )
            }) {
            Column(Modifier.padding(it)) {

                var selected by remember { mutableStateOf(SelectedLibraryItem.NONE) }
                val onChipSelected: (SelectedLibraryItem) -> Unit = { selectedItem ->
                    selected = selectedItem
                }
                ScrollableLibraryChips(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 11.dp),
                    currentItem = selected,
                    onSelected = onChipSelected
                )

                AnimatedContent(
                    targetState = selected,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(150, 150)) togetherWith
                                fadeOut(animationSpec = tween(150))

                    }, label = ""
                ) { selected ->
                    when (selected) {
                        SelectedLibraryItem.NONE -> LibraryFeedNoSelection(albumsProvider = {albums })

                        SelectedLibraryItem.SONGS -> {
                            SongList(
                                modifier = Modifier.fillMaxSize(),
                                playlist = songs,
                                expanded = false,
                                isPlayingProvider = { isPlaying },
                                currentSongProvider = { currentMediaItem }
                            ) { itemIndex: Int, mediaItemList: Playlist -> onSongSelected(itemIndex, mediaItemList) }
                        }

                        SelectedLibraryItem.FOLDERS -> FolderList(folders = folders)
                        SelectedLibraryItem.ALBUMS -> AlbumsList(modifier = Modifier.padding(11.dp),
                            albums = albums)
                        else -> androidx.compose.material3.Text("Unknown")
                    }
                }

            }
        }
        // content
    }



//    val libraryScreenContent : @Composable (PaddingValues) -> Unit = {
//        LibraryScreenContent(
//            scope = scope,
//            rootChildrenProvider =  { rootItems },
//            onItemSelectedMapProvider = { onItemSelectedMap },
//            playlist = { songs },
//            folders = { folders },
//            albums = { albums },
//            currentMediaItemProvider = { currentMediaItem },
//            isPlayingProvider = { isPlaying },
//            modifier = Modifier.padding(it)
//        )
//
//    }
//    val isLargeScreen = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded
//
//    val context = LocalContext.current
//    val libraryText = remember {context.getString(R.string.library) }

//    if (isLargeScreen) {
//        LargeLibraryScreen(
//            navDrawerContent = navDrawerContent,
//            topBar =  {
//                LargeAppBar(title = libraryText) {
//                    scope.launch {
//                        navController.navigate(Screen.SEARCH.name)
//                    }
//                }
//            },
//            bottomBar = bottomBar,
//            content = libraryScreenContent
//        )
//    } else {
//        val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//        SmallLibraryScreen(
//            bottomBar = bottomBar,
//            topBar = {
//                SmallAppBar(
//                    title = libraryText,
//                    onClickNavIcon = {
//                        scope.launch {
//                    if (drawerState.isClosed) {
//                        drawerState.open()
//                    } else {
//                        drawerState.close()
//                    }
//                }
//                    },
//                    onClickSearchIcon = {
//                        navController.navigate(Screen.SEARCH.name)
//                    }
//                )
//            },
//            navDrawerContent = navDrawerContent,
//            drawerState = drawerState,
//            content = libraryScreenContent
//        )
//    }
}

/**
 * The large Library Screen.
 *
 * @param navDrawerContent The content of the navigation drawer.
 * @param topBar The Top Bar.
 * @param bottomBar The Bottom Bar.
 * @param content The content of the Library Screen.
 */
@Composable
fun LargeLibraryScreen(
    navDrawerContent : @Composable () -> Unit = {},
    bottomBar : @Composable () -> Unit,
    topBar : @Composable () -> Unit,
    content : @Composable (PaddingValues) -> Unit = {}
) {
    // TODO: Replace with DismissibleNavigationDrawer when better support for content resizing.
    PermanentNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerContent = navDrawerContent,
    ) {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            content = { content(it)}
        )
    }
}

/**
 * The large Library Screen.
 *
 * @param topBar The Top Bar.
 * @param bottomBar The Bottom Bar.
 * @param navDrawerContent The composable content of the [ModalNavigationDrawer].
 * @param drawerState The [ModalNavigationDrawer] [DrawerState].
 * @param content The content of the Library Screen.
 */
@Composable
fun SmallLibraryScreen(
    bottomBar : @Composable () -> Unit,
    topBar : @Composable () -> Unit,
    navDrawerContent : @Composable () -> Unit = {},
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    content: @Composable (PaddingValues) -> Unit
) {

   ModalNavigationDrawer(
        drawerContent = navDrawerContent,
        drawerState = drawerState) {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            content = {content(it)}
        )
    }

}


@ExperimentalFoundationApi
@Composable
fun LibraryScreenContent(
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    pagerState: PagerState = rememberPagerState(initialPage = 0) { DEFAULT_NUMBER_OF_TABS },
    rootChildrenProvider: () -> Root,
    onItemSelectedMapProvider : () -> EnumMap<MediaItemType, Any > = { EnumMap(MediaItemType::class.java) },
    playlist : () -> Playlist = { Playlist(state= State.NOT_LOADED) },
    folders : () -> Folders = { Folders(state= State.NOT_LOADED) },
    albums : () -> Albums = { Albums(state= State.NOT_LOADED) },
    currentMediaItemProvider : () -> Song = { Song() },
    isPlayingProvider : () -> Boolean = {false}

) {

    Column(modifier.fillMaxHeight()) {
        LibraryTabs(
            pagerState = pagerState,
            rootChildrenProvider = rootChildrenProvider,
            scope = scope
        )

        Row(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
            TabBarPages(
                pagerState = pagerState,
                playlist = playlist,
                folders = folders,
                albums = albums,
                currentMediaItemProvider = currentMediaItemProvider,
                onItemSelectedMapProvider = onItemSelectedMapProvider,
                isPlayingProvider = isPlayingProvider
            )
        }
    }

}


/**
 * Displays the pages for each of the Home bar tabs.
 * @param pagerState The [PagerState] of the Tab Bar.
 * @param modifier The [Modifier].
 */
@OptIn(ExperimentalCoilApi::class, ExperimentalFoundationApi::class)
@Composable
fun TabBarPages(
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState() {3 },
    onItemSelectedMapProvider : () -> EnumMap<MediaItemType, Any > = { EnumMap(MediaItemType::class.java) },
    playlist : () -> Playlist = { Playlist(state= State.NOT_LOADED) },
    folders : () -> Folders = { Folders(state= State.NOT_LOADED) },
    albums : () -> Albums = { Albums(state= State.NOT_LOADED) },
    currentMediaItemProvider : () -> Song = { Song() },
    isPlayingProvider : () -> Boolean = {false}

) {
    val onItemSelectedMap = onItemSelectedMapProvider()
    val tabPages = listOf(MediaItemType.SONGS, MediaItemType.FOLDERS, MediaItemType.ALBUMS)

    Column(
        modifier = modifier) {
        HorizontalPager(
            state = pagerState,
        ) { pageIndex ->
            when (tabPages[pageIndex]) {
                MediaItemType.SONGS ->
                    SongList(
                        playlist = playlist(),
                        isPlayingProvider = isPlayingProvider,
                        currentSongProvider = { currentMediaItemProvider() }) {
                            itemIndex : Int, mediaItemList : Playlist ->
                            @Suppress("UNCHECKED_CAST")
                            val callable = onItemSelectedMap[MediaItemType.SONGS] as? (Int, Playlist) -> Unit
                            if (callable != null) {
                                callable(itemIndex, mediaItemList)
                            }
                    }
                MediaItemType.FOLDERS -> {
                    FolderList(folders = folders(),
                    modifier = Modifier.fillMaxSize()) {

                        val callable =
                            onItemSelectedMap[MediaItemType.FOLDERS] as? (Folder) -> Unit
                        if (callable != null) {
                            callable(it)
                        }
                    }
                }
                MediaItemType.ALBUMS -> {
                    AlbumsList(modifier = Modifier.fillMaxSize(),
                        albums = albums()) {
                            val callable =
                                onItemSelectedMap[MediaItemType.ALBUMS] as? (Album) -> Unit
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeAppBar(title : String,
                onSearchIconClicked : () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title)
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

@Composable
@Preview
private fun Navigation(
    windowSizeClass: WindowSizeClass = DEFAULT_WINDOW_CLASS_SIZE,
    navController : NavController = rememberNavController(),
    scope: CoroutineScope = rememberCoroutineScope(),
    drawerState : DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    content: @Composable () -> Unit = {}
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            ModalNavigationDrawer(
                drawerContent = {
                    NavigationDrawerContent(
                        navController = navController,
                        currentScreen = Screen.LIBRARY
                    )
                },
                drawerState = drawerState,
                content = content
            )
        }
        WindowWidthSizeClass.Medium -> {
            Row(Modifier.fillMaxSize()) {
                AppNavigationRail {
                    scope.launch {
                        if (drawerState.isOpen) {
                            drawerState.close()
                        } else {
                            drawerState.open()
                        }
                    }
                }
                content()
            }
        }

        WindowWidthSizeClass.Expanded -> {
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet {
                        NavigationDrawerContentInternal(navController,
                            currentScreen = Screen.LIBRARY)
                    }
                },
            ) {
                content()
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun LibraryAppBar(
    windowSize: WindowSizeClass = DEFAULT_WINDOW_CLASS_SIZE,
    onClickSearchIcon : () -> Unit = {},
    scrollBehavior : TopAppBarScrollBehavior =
    TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),

                onClickNavIcon : () -> Unit = {}) {
    val title = stringResource(id = R.string.library)
    if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
        SmallAppBar(
            title = title,
            scrollBehavior = scrollBehavior,
            onClickSearchIcon = onClickSearchIcon,
            onClickNavIcon = onClickNavIcon)
    } else {
        LargeTopAppBar(
            title = { androidx.compose.material3.Text(stringResource(id = R.string.library)) },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface
            ),
            scrollBehavior = scrollBehavior
        )
    }
}

