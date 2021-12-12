package com.github.goldy1992.mp3player.client.ui.screens

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.launch

@Composable
fun FolderScreen(
        folder : MediaBrowserCompat.MediaItem,
        navController: NavController,
        mediaBrowser : MediaBrowserAdapter,
        mediaController : MediaControllerAdapter
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val folderItems = remember(folder.mediaId) {mediaBrowser.subscribe(MediaItemUtils.getLibraryId(folder)!!) }

    Scaffold (
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar (
                        backgroundColor = MaterialTheme.colors.primary,
                        title = {
                            Column {
                                Text(text = MediaItemUtils.getDirectoryName(folder),
                                        style = MaterialTheme.typography.h6,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                )
                                Text(text = MediaItemUtils.getDirectoryPath(folder),
                                        style = MaterialTheme.typography.subtitle2,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis)
                            }
                        },

                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    navController.popBackStack()
                                }
                            }){
                                Icon(Icons.Filled.ArrowBack, "Back")
                            }
                        },
                        actions = {},
                        contentColor = MaterialTheme.colors.onPrimary
                )
            },
            bottomBar = {
                PlayToolbar(mediaController = mediaController) {
                    navController.navigate(Screen.NOW_PLAYING.name)
                }
            },

            content = {
                SongList(songs = folderItems.value!!, mediaControllerAdapter = mediaController) {
                    val libraryId = MediaItemUtils.getLibraryId(it)
                    Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                    mediaController.playFromMediaId(libraryId, null)
                }
            }
    )
}