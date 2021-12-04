package com.github.goldy1992.mp3player.client.ui.screens.main

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.BOTTOM_BAR_SIZE
import com.github.goldy1992.mp3player.client.ui.FolderList
import com.github.goldy1992.mp3player.client.ui.SongList
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.ui.lists.folder.SongsInFolderList
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isEmpty


@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun LargeMainScreenContent(
        mediaBrowser : MediaBrowserAdapter,
        scope: CoroutineScope,
        rootItems: List<MediaBrowserCompat.MediaItem>,
        mediaController: MediaControllerAdapter,
        mediaRepository: MediaRepository,
        viewModel : LargeMainScreenViewModel = viewModel()
) {

    if (rootItems.isEmpty()) {
        LoadingIndicator()
    } else {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = BOTTOM_BAR_SIZE)) {
            val currentNavigationItem by viewModel.currentNavigationItem.observeAsState(MediaItemType.SONGS)
            Divider(thickness = 5.dp, color = MaterialTheme.colors.background)

            Row(Modifier.fillMaxSize()) {
                RootItemsNavigationRail(
                        rootItems,
                        currentNavigationItem,
                        scope,
                        viewModel,
                        modifier = Modifier.Companion.align(Alignment.CenterVertically))
                NavigationPanel(
                        currentNavigationItem,
                        mediaBrowser,
                        mediaRepository,
                        mediaController,
                        viewModel,
                        modifier = Modifier.Companion.weight(0.5f))
                ChildNavigationPanel(
                        viewModel,
                        mediaController,
                        modifier = Modifier.Companion.weight(0.5f))

            }
        }
    }

}

@Composable
fun ChildNavigationPanel(viewModel: LargeMainScreenViewModel,
                         mediaController: MediaControllerAdapter,
                         modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val childItems by viewModel.mediaItemChildren.observeAsState()
        val mediaItemSelected by viewModel.mediaItemSelected.observeAsState()
        if (MediaItemUtils.isEmptyMediaItem(mediaItemSelected)) {
            Text(text = "Select a media item")
        }
        when (MediaItemUtils.getMediaItemType(mediaItemSelected)) {
            MediaItemType.SONG -> Text(text = "Selected ${MediaItemUtils.getTitle(mediaItemSelected!!)}")
            MediaItemType.FOLDER -> {
                val folderItems = childItems
                val mis = mediaItemSelected
                if (folderItems != null && mis != null) {
                    SongsInFolderList(folder = mis, songsInFolders = folderItems) {
                        val libraryId = MediaItemUtils.getLibraryId(it)
                        Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                        mediaController.playFromMediaId(libraryId, null)
                    }
                }
            }
            else -> Text(text = "selected ${Constants.UNKNOWN}")
        }
    }
}

@Composable
fun NavigationPanel(currentNavigationItem: MediaItemType?,
                    mediaBrowser: MediaBrowserAdapter,
                    mediaRepository: MediaRepository,
                    mediaController: MediaControllerAdapter,
                    viewModel: LargeMainScreenViewModel,
                    modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        when (currentNavigationItem) {
            MediaItemType.SONGS -> {
                val songs = mediaRepository.itemMap[MediaItemType.SONGS]
                if (songs == null) {
                    CircularProgressIndicator()
                } else {
                    SongList(songs = songs.value!!) {
                        val libraryId = MediaItemUtils.getLibraryId(it)
                        Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                        mediaController.playFromMediaId(libraryId, null)
                        viewModel.mediaItemSelected.postValue(it)
                    }
                }
            }
            MediaItemType.FOLDERS -> {
                val folders = mediaRepository.itemMap[MediaItemType.FOLDERS]
                if (folders == null) {
                    CircularProgressIndicator()
                } else {
                    FolderList(foldersData = folders) {
                        mediaRepository.currentFolder = it
                        viewModel.mediaItemChildren = mediaBrowser.subscribe(MediaItemUtils.getLibraryId(it)!!)
                        viewModel.mediaItemSelected.postValue(it!!)
                    }
                }
            }
            else -> {}
        }

    }
}

@Composable
fun RootItemsNavigationRail(rootItems: List<MediaBrowserCompat.MediaItem>?,
                            currentNavigationItem : MediaItemType,
                            scope: CoroutineScope,
                            viewModel: LargeMainScreenViewModel,
                            modifier : Modifier = Modifier) {
    NavigationRail(modifier = modifier) {
        if (isEmpty(rootItems)) {
            LoadingIndicator()
        } else {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                rootItems?.forEach() {
                    NavigationRailItem(
                        selected = currentNavigationItem == MediaItemUtils.getRootMediaItemType(it),
                        onClick = {
                            scope.launch {
                                val selectedItem = MediaItemUtils.getRootMediaItemType(it)
                                if (currentNavigationItem != selectedItem) {
                                    viewModel.currentNavigationItem.postValue(MediaItemUtils.getRootMediaItemType(it))
                                    viewModel.mediaItemSelected.postValue(MediaItemUtils.getEmptyMediaItem())
                                }
                            }
                        },
                        label = { Text(MediaItemUtils.getTitle(it)) },
                        icon = {
                            when (MediaItemUtils.getRootMediaItemType(it)) {
                                MediaItemType.SONGS -> Icon(Icons.Filled.MusicNote, "")
                                MediaItemType.FOLDERS -> Icon(Icons.Filled.Folder, "")
                                else -> Icon(Icons.Filled.MusicNote, "")
                            }
                        },
                    )
                }
            }
        }
    }
}