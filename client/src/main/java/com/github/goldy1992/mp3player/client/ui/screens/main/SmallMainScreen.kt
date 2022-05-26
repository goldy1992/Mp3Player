package com.github.goldy1992.mp3player.client.ui.screens.main

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.BOTTOM_BAR_SIZE
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.google.accompanist.pager.ExperimentalPagerApi
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
//            TabBarPages(
//                    navController = navController,
//                    mediaRepository = mediaRepository,
//                    mediaController = mediaController,
//                    pagerState = pagerState,
//                    rootItems = rootItems
//            )
        }
    }

}

