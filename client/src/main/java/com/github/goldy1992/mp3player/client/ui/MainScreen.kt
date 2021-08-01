package com.github.goldy1992.mp3player.client.ui

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getRootMediaItemType
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
               mediaRepository: MediaRepository,
               mediaController: MediaControllerAdapter,
               scaffoldState: ScaffoldState = rememberScaffoldState(),
               pagerState: PagerState = rememberPagerState(pageCount = mediaRepository.rootItems.value!!.size)
) {
    val rootItems: List<MediaItem> by mediaRepository.rootItems.observeAsState(listOf(MediaItemUtils.getEmptyMediaItem()))
 //   val pagerState = rememberPagerState(pageCount = rootItems.size)
//    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
        HomeAppBar (
            navController = navController,
            pagerState = pagerState,
            scope = scope,
            scaffoldState = scaffoldState,
            rootItems = rootItems)
        },
        bottomBar = {
            PlayToolbar(mediaController = mediaController) {
                navController.navigate(Screen.NOW_PLAYING.name)
            }
        },

        content = {
            TabBarPages(navController = navController,
                        mediaRepository = mediaRepository,
                        mediaController = mediaController,
                        pagerState = pagerState,
                        rootItems = rootItems)
        },
        drawerContent = {
            NavigationDrawer(navController = navController)
        })
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
                rootItems: List<MediaItem>) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = BOTTOM_BAR_SIZE)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(),

            ) { pageIndex ->

            val currentItem = rootItems[pageIndex]

            when (MediaItemUtils.getRootMediaItemType(currentItem)) {
                MediaItemType.SONGS -> {
                    val songs = mediaRepository.itemMap[MediaItemType.SONGS]
                    if (songs == null) {
                        CircularProgressIndicator()
                    } else {
                        SongList(songsData = songs, mediaController = mediaController)
                    }
                }
                MediaItemType.FOLDERS -> {
                    val folders = mediaRepository.itemMap[MediaItemType.FOLDERS]
                    if (folders == null) {
                        CircularProgressIndicator()
                    } else {
                        FolderList(foldersData = folders, navController = navController, mediaRepository = mediaRepository)
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
    pagerState: PagerState,
    scope : CoroutineScope,
    scaffoldState: ScaffoldState,
    rootItems: List<MediaItem>) {
    val navigationDrawerIconDescription = stringResource(id = R.string.navigation_drawer_menu_icon)
    Column {
        TopAppBar(
            title = {
                Text(text = "MP3 Player")
            },
            backgroundColor = MaterialTheme.colors.primary,
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        if (scaffoldState.drawerState.isClosed) {
                            scaffoldState.drawerState.open()
                        }
                    }
                }, modifier = Modifier.semantics { contentDescription = navigationDrawerIconDescription })
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
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator =  { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }) {
            if (rootItemsLoaded(rootItems)) {
                rootItems.forEachIndexed { index, item ->
                    Tab(content = {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                            text = getRootItemText(mediaItemType = getRootMediaItemType(item = item)!!),
                            style = MaterialTheme.typography.button
                        )
                    },
                        onClick = {                    // Animate to the selected page when clicked
                            scope.launch {
                                Log.i("MainScreen", "Clicked to go to index ${index}, string: ${item.mediaId} ")
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        selected = pagerState.currentPage == index
                    )
                }
            } else {
                Tab(
                    content = { CircularProgressIndicator() },
                    onClick = {                   },
                    selected = pagerState.currentPage == 0)
            }
        }
    }
} // HomeAppBar

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