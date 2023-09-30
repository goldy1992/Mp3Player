package com.github.goldy1992.mp3player.client.ui.screens.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.media.Albums
import com.github.goldy1992.mp3player.client.models.media.Folders
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.ui.components.AlbumArtAsync
import com.github.goldy1992.mp3player.client.ui.components.feed.Feed
import com.github.goldy1992.mp3player.client.ui.components.feed.title
import com.github.goldy1992.mp3player.client.ui.components.navigation.AppNavigationRail
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.lists.albums.AlbumsList
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderList
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.EnumMap

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Preview(widthDp = 800, heightDp = 600)
@Composable
fun LibraryScreenMedium(
    bottomBar : @Composable () -> Unit = {},
    currentMediaItemProvider : () -> Song = { Song() },
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    isPlayingProvider : () -> Boolean = {false},
    root: () -> Root = { Root.NOT_LOADED},
    songs : () ->Playlist = { Playlist.NOT_LOADED},
    folders : () -> Folders = { Folders.NOT_LOADED},
    albums : () -> Albums = { Albums.NOT_LOADED },
    onSelectedMap: () -> EnumMap<MediaItemType, Any> = { EnumMap(MediaItemType::class.java) },
    navController: NavController = rememberNavController(),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
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
            val scrollBehavior =
                TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

                topBar = {
                    LargeTopAppBar(
                        title = { Text(stringResource(id = R.string.library)) },
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
                            SelectedLibraryItem.NONE -> LibraryFeedNoSelection(albumsProvider = albums)

                            SelectedLibraryItem.SONGS -> {
                                SongList(
                                    modifier = Modifier.fillMaxSize(),
                                    playlist = songs(),
                                    expanded = false,
                                    isPlayingProvider = isPlayingProvider,
                                    currentSongProvider = currentMediaItemProvider
                                ) { itemIndex: Int, mediaItemList: Playlist ->
                                    val callable =
                                        onSelectedMap()[MediaItemType.SONGS] as (Int, Playlist) -> Unit
                                    callable(itemIndex, mediaItemList)
                                }
                            }

                            SelectedLibraryItem.FOLDERS -> FolderList(folders = folders())
                            SelectedLibraryItem.ALBUMS -> AlbumsList(modifier = Modifier.padding(11.dp),
                                albums = albums())
                            else -> Text("Unknown")
                        }
                    }

                }
            }
        }
    }
}

 @Preview
 @Composable
 fun LibraryFeedNoSelection(albumsProvider: () -> Albums = {Albums.NOT_LOADED}) {
     val state = rememberLazyGridState()

     Feed(
         columns = GridCells.Fixed(3),
         state = state,
         contentPadding = PaddingValues(
             horizontal = 32.dp,
             vertical = 48.dp
         ),
         horizontalArrangement = Arrangement.spacedBy(8.dp),
         verticalArrangement = Arrangement.spacedBy(8.dp)

     ) {
         title(contentType = "feed-title") {
             Text(
                 text = "Recently Played",
                 style = MaterialTheme.typography.headlineLarge,
                 modifier = Modifier.padding(PaddingValues(vertical = 24.dp))
             )
         }
         
        val albums = albumsProvider()

        items(albums.albums.size, key = {  albums.albums[it].id }) {
            val currentAlbum = albums.albums[it]
            AlbumArtAsync(modifier= Modifier.size(150.dp), uri = currentAlbum.artworkUri, contentDescription = "")
         }
     }
 }

