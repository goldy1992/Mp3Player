package com.github.goldy1992.mp3player.client.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.QueueItemUtils
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun NowPlayingScreen(navController: NavController,
               mediaController: MediaControllerAdapter
) {
    val queue by mediaController.queue.observeAsState()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = queue!!.size, initialPage = mediaController.calculateCurrentQueuePosition())
    val scaffoldState = rememberScaffoldState()

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar (
                backgroundColor = MaterialTheme.colors.primary,
                title = {
                    val metadata by mediaController.metadata.observeAsState()
                    Column {
                        Text(text = metadata?.description?.title.toString(),
                            style = MaterialTheme.typography.h6
                            )
                        Text(text = metadata?.description?.subtitle.toString(),
                            style = MaterialTheme.typography.subtitle2)
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
                // do Nothing
            }
        },

        content = {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(bottom = BOTTOM_BAR_SIZE)) {

                LaunchedEffect(pagerState.currentPage) {
                    Log.i("NOW_PLAYING", "current page changed")
                    val newPosition = pagerState.currentPage
                    val currentQueuePosition = mediaController.calculateCurrentQueuePosition()
                    if (newPosition < 0 || currentQueuePosition == newPosition) {

                    }
                    else {
                        if (isSkipToNext(newPosition, currentQueuePosition)) {
                            mediaController.skipToNext()
                        } else if (isSkipToPrevious(newPosition, currentQueuePosition)) {
                            mediaController.seekTo(0)
                            mediaController.skipToPrevious()
                        }
                    }
                }


                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth(),

                    ) { pageIndex ->
                    val item = queue!![pageIndex]

                    Image(painter = rememberGlidePainter(
                        request = QueueItemUtils.getAlbumArtUri(item)),
                        contentDescription = "Album Art")
                }
            }
        }
    )
}

private fun isSkipToNext(newPosition: Int, currentPosition : Int): Boolean {
    return newPosition == (currentPosition + 1)
}

private fun isSkipToPrevious(newPosition: Int, currentPosition : Int): Boolean {
    return newPosition == (currentPosition - 1)
}