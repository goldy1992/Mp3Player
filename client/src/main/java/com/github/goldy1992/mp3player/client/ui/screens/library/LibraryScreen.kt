package com.github.goldy1992.mp3player.client.ui.screens.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.ui.DEFAULT_WINDOW_CLASS_SIZE
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.navigation.AppNavigationRail
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.lists.albums.AlbumsList
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderList
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.utils.NavigationUtils
import com.github.goldy1992.mp3player.client.utils.NavigationUtils.showNavRail
import com.github.goldy1992.mp3player.client.utils.NavigationUtils.toggleNavigationDrawer
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val LOG_TAG = "LibraryScreen"

/**
 * The Library Screen of the app.
 *
 * @param navController The [NavController].
 * @param viewModel The [LibraryScreenViewModel].
 * @param windowSize The [WindowSizeClass].
 * @param scope The [CoroutineScope].
 */
@Composable
fun SharedTransitionScope.LibraryScreen(
    navController: NavController = rememberNavController(),
    viewModel: LibraryScreenViewModel = viewModel(),
    windowSize: WindowSizeClass = DEFAULT_WINDOW_CLASS_SIZE,
    scope: CoroutineScope = rememberCoroutineScope(),
    animatedContentScope: AnimatedVisibilityScope

) {
    val selectedLibraryChip by viewModel.selectedChip.collectAsState()
    val root by viewModel.root.collectAsState()
    val songs by viewModel.songs.collectAsState()
    val folders by viewModel.folders.collectAsState()
    val albums by viewModel.albums.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()
    val drawerState : DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    val onSongSelected: (Int, Playlist) -> Unit = { itemIndex, mediaItemList ->
        viewModel.playPlaylist(mediaItemList, itemIndex)
    }
    val onFolderSelected: (Folder) -> Unit = { NavigationUtils.navigate(navController, it) }
    val onAlbumSelected: (Album) -> Unit = { NavigationUtils.navigate(navController, it) }

    Navigation(
        navController = navController,
        windowSizeClass = windowSize,
        drawerState = drawerState,
        scope = scope
    ) {
        val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            topBar = {
                LibraryAppBar(
                    windowSize = windowSize,
                    scrollBehavior = scrollBehavior,
                    onClickNavIcon = { scope.launch { toggleNavigationDrawer(drawerState) } },
                    onClickSearchIcon = { navController.navigate(Screen.SEARCH.name) }
                )
            },
            bottomBar = {
                PlayToolbar(
                    animatedVisibilityScope = animatedContentScope,
                    playbackState = playbackState,
                    onClickBar = { navController.navigate(Screen.NOW_PLAYING.name) },
                    windowSizeClass = windowSize,
                )
            }) {
            Column(Modifier.padding(it)) {
                val onChipSelected: (SelectedLibraryItem) -> Unit = { selectedItem ->
                   viewModel.setSelectedChip(selectedItem)
                }
                ScrollableLibraryChips(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 11.dp),
                    currentItem =  selectedLibraryChip ,
                    onSelected = onChipSelected
                )

                AnimatedContent(
                    targetState = selectedLibraryChip,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(150, 150)) togetherWith
                                fadeOut(animationSpec = tween(150))

                    }, label = "animatedContentLibraryChips"
                ) { selected ->
                    when (selected) {
                        SelectedLibraryItem.NONE -> LibraryFeedNoSelection(albumsProvider = { albums })

                        SelectedLibraryItem.SONGS -> {
                            SongList(
                                modifier = Modifier.fillMaxSize(),
                                playlist = songs,
                                expanded = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded,
                                isPlayingProvider = { playbackState.isPlaying },
                                currentSongProvider = { playbackState.currentSong }
                            ) { itemIndex: Int, mediaItemList: Playlist ->
                                onSongSelected(
                                    itemIndex,
                                    mediaItemList
                                )
                            }
                        }

                        SelectedLibraryItem.FOLDERS -> FolderList(folders = folders,
                            onFolderSelected = onFolderSelected)
                        SelectedLibraryItem.ALBUMS -> AlbumsList(
                            modifier = Modifier.padding(5.dp),
                            windowSize = windowSize,
                            onAlbumSelected = onAlbumSelected,
                            albums = albums,
                            sharedTransitionScope = this@LibraryScreen,
                            animatedVisibilityScope = animatedContentScope
                        )

                        else -> Text("Unknown")
                    }
                }
            }
        }
    }
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
    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                currentScreen = Screen.LIBRARY
            )
        },
        drawerState = drawerState,
        content = {
            Row(Modifier.fillMaxSize()) {
                if (showNavRail(windowSizeClass)) {
                    AppNavigationRail(
                        navController = navController,
                        currentScreen = Screen.LIBRARY
                    ) {
                        scope.launch { toggleNavigationDrawer(drawerState) }
                    }
                }
                content()
            }
        }
    )
}


@Composable
@Preview
private fun LibraryAppBar(
    windowSize: WindowSizeClass = DEFAULT_WINDOW_CLASS_SIZE,
    onClickSearchIcon : () -> Unit = {},
    scrollBehavior : TopAppBarScrollBehavior =
    TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),

                onClickNavIcon : () -> Unit = {}) {
    val title = stringResource(id = R.string.library)

    val useSmallAppBar = windowSize.widthSizeClass == WindowWidthSizeClass.Compact ||
            windowSize.heightSizeClass == WindowHeightSizeClass.Compact
    if (useSmallAppBar) {
        SmallAppBar(
            title = title,
            showNavIcon = !showNavRail(windowSize),
            scrollBehavior = scrollBehavior,
            onClickSearchIcon = onClickSearchIcon,
            onClickNavIcon = onClickNavIcon)
    } else {
        LargeTopAppBar(
            title = { Text(stringResource(id = R.string.library)) },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface
            ),
            actions = {
                IconButton(onClick = onClickSearchIcon) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
}

