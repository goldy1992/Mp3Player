package com.github.goldy1992.mp3player.client.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.ui.components.feed.Feed
import com.github.goldy1992.mp3player.client.ui.components.feed.title
import com.github.goldy1992.mp3player.client.ui.components.navigation.AppNavigationRail
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview(widthDp = 800, heightDp = 600)
@Composable
fun LibraryScreenMedium(
    bottomBar : @Composable () -> Unit = {},
    currentMediaItemProvider : () -> Song = { Song() },
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    isPlayingProvider : () -> Boolean = {false},
    library: Library = Library.DEFAULT,
    navController: NavController = rememberNavController(),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                currentScreen = Screen.LIBRARY
            )
        },
        drawerState = drawerState
    ) {


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
             val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

             Scaffold(
                 modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

                 topBar = {
                     LargeTopAppBar(
                        title = { Text(stringResource(id = R.string.library))},
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                           containerColor = MaterialTheme.colorScheme.surface,
                            scrolledContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        scrollBehavior = scrollBehavior
                     )

                 }
             ) {
                 Column(Modifier.padding(it)) {

                     var selected by remember { mutableStateOf(SelectedLibraryItem.NONE) }
                     val onChipSelected : (SelectedLibraryItem) -> Unit = { selectedItem ->
                         selected = selectedItem
                     }
                     ScrollableLibraryChips(
                         modifier = Modifier.fillMaxWidth().background(Color.Yellow),
                         currentItem = selected,
                         onSelected = onChipSelected
                     )
                     val state = rememberLazyGridState()
                     Feed(
                         columns = GridCells.Fixed(1),
                         state = state,
                         contentPadding = PaddingValues(horizontal = 32.dp, vertical = 48.dp),
                         horizontalArrangement = Arrangement.spacedBy(8.dp),
                         verticalArrangement = Arrangement.spacedBy(8.dp)

                     ) {
                         title(contentType = "feed-title") {
                             Text(
                                 text = "Explore",
                                 style = MaterialTheme.typography.headlineLarge,
                                 modifier = Modifier.padding(PaddingValues(vertical = 24.dp))
                             )
                         }

                         for (i in 1..30) {
                             item {
                                 Text("item $i")
                             }
                         }
                     }
                 }
             }
         }
    }
}

