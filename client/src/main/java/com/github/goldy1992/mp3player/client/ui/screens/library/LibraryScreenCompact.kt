package com.github.goldy1992.mp3player.client.ui.screens.library

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.ui.DEFAULT_NUMBER_OF_TABS
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Preview
@Composable
fun LibraryScreenCompact(
    bottomBar : @Composable () -> Unit = {},
    currentMediaItemProvider : () -> Song = { Song() },
    isPlayingProvider : () -> Boolean = {false},
    library: Library = Library.DEFAULT,
    navController: NavController = rememberNavController(),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val context = LocalContext.current
    val libraryText = remember {context.getString(R.string.library) }
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val pagerState : PagerState = rememberPagerState(initialPage = 0) { DEFAULT_NUMBER_OF_TABS }

    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                currentScreen = Screen.LIBRARY
        ) },
        drawerState = drawerState) {

        Scaffold(
            topBar = {
                SmallAppBar(
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
            bottomBar = bottomBar
        ) {
            Column(Modifier
                .fillMaxSize()
                .padding(it)) {
                LibraryTabs(
                    pagerState = pagerState,
                    rootChildrenProvider = library.root,
                    scope = scope
                )

                Row(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                    TabBarPages(
                        pagerState = pagerState,
                        playlist = library.songs,
                        folders = library.folders,
                        albums = library.albums,
                        currentMediaItemProvider = currentMediaItemProvider,
                        onItemSelectedMapProvider = library.onSelectedMap,
                        isPlayingProvider = isPlayingProvider
                    )
                }

            }
        }
    }
}