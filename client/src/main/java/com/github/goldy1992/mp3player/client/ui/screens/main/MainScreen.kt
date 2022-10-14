package com.github.goldy1992.mp3player.client.ui.screens.main

import androidx.media3.common.MediaItem
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.ui.NavigationDrawer
import com.github.goldy1992.mp3player.client.ui.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.viewmodels.MainScreenViewModel
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
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
               viewModel: MainScreenViewModel = viewModel(),
               scaffoldState: ScaffoldState = rememberScaffoldState(),
               pagerState: PagerState = rememberPagerState(initialPage = 0)
) {

    Text("Welcome to the main screen")
//    val isExpanded = windowSize == WindowSize.Expanded
//    val rootItems: List<MediaItem> by mediaRepository.rootItems.observeAsState(emptyList())
//    val scope = rememberCoroutineScope()
//
//    CustomScaffold(            scaffoldState = scaffoldState,
//        navController = navController,
//        scope = scope,
//        mediaController = mediaController,
//        extendTopAppBar = {
//            if (!isExpanded) {
//                MainTabs(pagerState = pagerState, rootItems = rootItems, scope = scope)
//            }
//        },
//        content = {
//            if (isExpanded) {
//                LargeMainScreenContent(
//                    mediaBrowser = mediaBrowserAdapter,
//                    scope = scope,
//                    rootItems = rootItems,
//                    mediaController = mediaController,
//                    mediaRepository = mediaRepository
//                )
//                // create large main screen
//            } else {
//                SmallMainScreenContent(
//                    navController = navController,
//                    pagerState = pagerState,
//                    rootItems = rootItems,
//                    mediaController = mediaController,
//                    mediaRepository = mediaRepository
//                )
//            }
//        }
//    )
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
    )
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

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
private fun CustomScaffold(
    scaffoldState: ScaffoldState,
    navController: NavController,
    scope: CoroutineScope,
    mediaController: MediaControllerAdapter,
    isPlayingFlow: IsPlayingFlow,
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
            PlayToolbar(mediaController = mediaController, isPlayingFlow = isPlayingFlow, scope = scope) {
                navController.navigate(Screen.NOW_PLAYING.name)
            }
        },
        drawerContent = {
            NavigationDrawer(navController = navController)
        },
        content = content)
}