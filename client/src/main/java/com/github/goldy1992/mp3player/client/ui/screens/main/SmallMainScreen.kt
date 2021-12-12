package com.github.goldy1992.mp3player.client.ui.screens.main

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.BOTTOM_BAR_SIZE
import com.github.goldy1992.mp3player.client.ui.lists.folders.FolderList
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun SmallMainScreenContent(
        navController: NavController,
        pagerState: PagerState,
        rootItems: List<MediaBrowserCompat.MediaItem>,
        mediaController: MediaControllerAdapter,
        mediaRepository: MediaRepository
) {
    Row(
            Modifier
                    .fillMaxSize()
                    .padding(bottom = BOTTOM_BAR_SIZE) ) {
        if (rootItems.isEmpty() ) {
            LoadingIndicator()
        } else {
            TabBarPages(
                    navController = navController,
                    mediaRepository = mediaRepository,
                    mediaController = mediaController,
                    pagerState = pagerState,
                    rootItems = rootItems
            )
        }
    }

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
                rootItems: List<MediaItem>,
                modifier: Modifier = Modifier) {
    Column(
            modifier = modifier) {
        HorizontalPager(
                state = pagerState,
                modifier = Modifier
                        .fillMaxWidth(),
                count = mediaRepository.rootItems.value?.size ?: 0
        ) { pageIndex ->

            val currentItem = rootItems[pageIndex]

            when (MediaItemUtils.getRootMediaItemType(currentItem)) {
                MediaItemType.SONGS -> {
                    val songs = mediaRepository.itemMap[MediaItemType.SONGS]
                    if (songs == null) {
                        CircularProgressIndicator()
                    } else {
                        SongList(songs = songs.value!!, mediaControllerAdapter = mediaController) {
                            val libraryId = MediaItemUtils.getLibraryId(it)
                            Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                            mediaController.playFromMediaId(libraryId, null)
                        }                 }
                }
                MediaItemType.FOLDERS -> {
                    val folders = mediaRepository.itemMap[MediaItemType.FOLDERS]
                    if (folders == null) {
                        CircularProgressIndicator()
                    } else {
                        FolderList(foldersData = folders) {
                            mediaRepository.currentFolder = it
                            navController.navigate(Screen.FOLDER.name)
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