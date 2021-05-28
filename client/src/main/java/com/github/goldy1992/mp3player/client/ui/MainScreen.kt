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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun MainScreen(navController: NavController,
               mediaRepository: MediaRepository,
               mediaController: MediaControllerAdapter
) {
    val rootItems: List<MediaItem> by mediaRepository.rootItems.observeAsState(listOf(MediaItemUtils.getEmptyMediaItem()))
    val pagerState = rememberPagerState(pageCount = rootItems.size)
    val scaffoldState = rememberScaffoldState()
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
                navController.navigate(NOW_PLAYING_SCREEN)
            }
        },

        content = {
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
                            SongList(songsData = mediaRepository.itemMap[MediaItemType.SONGS]!!, mediaController = mediaController)
                        }
                        MediaItemType.FOLDERS -> {
                            FolderList(foldersData = mediaRepository.itemMap[MediaItemType.FOLDERS]!!, navController = navController, mediaRepository = mediaRepository)
                        }
                        else -> {
                            Log.i("mainScreen", "unrecognised Media Item")
                        }
                    }
                }
            }
        },
        drawerContent = {
            NavigationDrawer(navController = navController)
        })

}

@ExperimentalPagerApi
@Composable
fun HomeAppBar(
    navController: NavController,
    pagerState: PagerState,
    scope : CoroutineScope,
    scaffoldState: ScaffoldState,
    rootItems: List<MediaItem>) {

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
                }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu Btn")
                }
            },
            actions = {
                IconButton(onClick = { navController.navigate(SEARCH_SCREEN) }) {
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
                            text = MediaItemUtils.getRootMediaItemType(item = item)!!.name,
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

fun rootItemsLoaded(items : List<MediaItem>) : Boolean {
    return !(items.isEmpty() || MediaItemUtils.getMediaId(items.first()) == Constants.EMPTY_MEDIA_ITEM_ID)
}