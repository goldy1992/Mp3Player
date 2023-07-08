@file:OptIn(ExperimentalAnimationApi::class)

package com.github.goldy1992.mp3player.client.ui.screens.library

import android.util.Base64
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
import com.github.goldy1992.mp3player.client.ui.*
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.lists.albums.AlbumsList
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderList
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.utils.MediaItemNameUtils
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
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
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun LibraryScreen(navController: NavController = rememberAnimatedNavController(),
                  pagerState: PagerState = rememberPagerState(initialPage = 0),
                  viewModel: LibraryScreenViewModel = viewModel(),
                  windowSize: WindowSize = WindowSize.Compact,
                  scope: CoroutineScope = rememberCoroutineScope()
) {
    val rootItems by viewModel.root.collectAsState()
    val songs by viewModel.songs.collectAsState()
    val folders by viewModel.folders.collectAsState()
    val albums by viewModel.albums.collectAsState()
    val isPlaying by viewModel.isPlaying.state().collectAsState()
    val currentMediaItem by viewModel.currentSong.state().collectAsState()

    val onSongSelected : (Int, Playlist) -> Unit =  {
            itemIndex, mediaItemList ->
        viewModel.playPlaylist(mediaItemList, itemIndex)
    }
    val onFolderSelected : (Folder) -> Unit = {
        val encodedFolderLibraryId = it.encodedLibraryId
        val encodedFolderPath = it.encodedPath
        val folderName = it.name
        Log.d(LOG_TAG, "onFolderSelected() Folder name: $folderName")
        navController.navigate(
            Screen.FOLDER.name
                    + "/" + encodedFolderLibraryId
                    + "/" + folderName
                    + "/" + encodedFolderPath)
    }

    val onAlbumSelected : (Album) -> Unit = {
        val albumId = it.id
        val albumTitle = it.title
        val albumArtist = it.artist
        val albumArtUriBase64 = Base64.encodeToString(it.artworkUri.toString().encodeToByteArray(), Base64.DEFAULT)
        Log.d(LOG_TAG, "onAlbumSelected() Album $albumTitle uri: ${it.artworkUri}")
        navController.navigate(
            Screen.ALBUM.name
                    + "/" + albumId
                    + "/" + albumTitle
                    + "/" + albumArtist
                    + "/" + albumArtUriBase64)
    }

    val onItemSelectedMap : EnumMap<MediaItemType, Any> = EnumMap(MediaItemType::class.java)
    onItemSelectedMap[MediaItemType.SONGS] = onSongSelected
    onItemSelectedMap[MediaItemType.FOLDERS] = onFolderSelected
    onItemSelectedMap[MediaItemType.ALBUMS] = onAlbumSelected

    val navDrawerContent : @Composable () -> Unit = {
        NavigationDrawerContent(
            navController = navController,
            currentScreen = Screen.LIBRARY
        )
    }

    val libraryScreenContent : @Composable (PaddingValues) -> Unit = {
        LibraryScreenContent(
            scope = scope,
            pagerState = pagerState,
            rootChildrenProvider =  { rootItems },
            onItemSelectedMapProvider = { onItemSelectedMap },
            playlist = { songs },
            folders = { folders },
            albums = { albums },
            currentMediaItemProvider = { currentMediaItem },
            isPlayingProvider = { isPlaying },
            modifier = Modifier.padding(it)
        )

    }
    val isLargeScreen = windowSize == WindowSize.Expanded
    val bottomBar : @Composable () -> Unit = {
        PlayToolbar(
            isPlayingProvider = { isPlaying },
            onClickPlay = { viewModel.play()
                          Log.v(LOG_TAG, "PlayToolbar.onClickPlay() clicked play")},
            onClickPause = {viewModel.pause()
                           Log.v(LOG_TAG, "PlayToolbar.onClickPause() clicked pause")},
            onClickSkipPrevious = { viewModel.skipToPrevious() },
            onClickSkipNext = { viewModel.skipToNext() },
            onClickBar = { navController.navigate(Screen.NOW_PLAYING.name)},
            currentSongProvider = { currentMediaItem }
        )
    }
    val context = LocalContext.current
    val libraryText = remember {context.getString(R.string.library) }

    if (isLargeScreen) {
        LargeLibraryScreen(
            navDrawerContent = navDrawerContent,
            topBar =  {
                LargeAppBar(title = libraryText) {
                    scope.launch {
                        navController.navigate(Screen.SEARCH.name)
                    }
                }
            },
            bottomBar = bottomBar,
            content = libraryScreenContent
        )
    } else {
        val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        SmallLibraryScreen(
            bottomBar = bottomBar,
            topBar = {
                SmallLibraryAppBar(
                    title = libraryText,
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
            },
            navDrawerContent = navDrawerContent,
            drawerState = drawerState,
            content = libraryScreenContent
        )
    }
}

/**
 * The large Library Screen.
 *
 * @param navDrawerContent The content of the navigation drawer.
 * @param topBar The Top Bar.
 * @param bottomBar The Bottom Bar.
 * @param content The content of the Library Screen.
 */
@ExperimentalMaterialApi
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
private fun LibraryTabs(
    pagerState: PagerState,
    rootChildrenProvider:  () -> Root,
    scope: CoroutineScope
) {

    val rootItemsState = rootChildrenProvider()

    if (rootItemsState.state == State.LOADED) {
        val rootItems = rootItemsState.childMap
        Column {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                rootItems.keys.forEachIndexed { index, key ->
                    val item = rootItems[key]
                    if (item != null) {
                        val isSelected = index == pagerState.currentPage
                        val context = LocalContext.current
                        Tab(
                            selected = isSelected,
                            modifier = Modifier
                                .height(48.dp)
                                .padding(start = 10.dp, end = 10.dp),
                            content = {
                                Text(
                                    text = MediaItemNameUtils.getMediaItemTypeName(
                                        context,
                                        item.type
                                    )
                                        .uppercase(),//getRootMediaItemType(item = item)?.name ?: Constants.UNKNOWN,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                )
                            },
                            onClick = {                    // Animate to the selected page when clicked
                                scope.launch {
                                    Log.d(
                                        LOG_TAG,
                                        "Clicked to go to index ${index}, string: ${item.id} "
                                    )
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}


@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun LibraryScreenContent(
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    pagerState: PagerState = rememberPagerState(initialPage = 0),
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
    pagerState: PagerState = rememberPagerState(),
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
            pageCount = tabPages.size
        ) { pageIndex ->
            when (tabPages[pageIndex]) {
                MediaItemType.SONGS ->
                    SongList(
                        playlist = playlist(),
                        isPlayingProvider = isPlayingProvider,
                        currentSongProvider = { currentMediaItemProvider() }) {
                            itemIndex : Int, mediaItemList : Playlist ->
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