package com.github.goldy1992.mp3player.client.ui

import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleButton
import com.github.goldy1992.mp3player.commons.QueueItemUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import org.apache.commons.lang3.ObjectUtils.isEmpty

@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun NowPlayingScreen(
    navController: NavController,
    mediaController: MediaControllerAdapter,
    scope : CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState()

) {
    val songTitleDescription = stringResource(id = R.string.song_title)
    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar (
                backgroundColor = MaterialTheme.colors.primary,
                title = {
                    val metadata by mediaController.metadata.observeAsState()
                    val title : String = metadata?.description?.title.toString() ?: ""
                    val artist : String = metadata?.description?.subtitle.toString() ?: ""
                    Column {
                        Text(text = title,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.semantics {
                                contentDescription = songTitleDescription
                            }
                            )
                        Text(text = artist,
                            style = MaterialTheme.typography.subtitle2)
                    }
                },

                navigationIcon = {
                    NavUpButton(
                        navController = navController,
                        scope = scope)
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
            val nowPlayingDescr = stringResource(id = R.string.now_playing_screen)
            Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = BOTTOM_BAR_SIZE)
                        .semantics {
                            contentDescription = nowPlayingDescr
                        }
            ) {

                Column(
                    modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.background)
                ) {

                    SpeedController(mediaController = mediaController)
                    ViewPager(mediaController = mediaController)
                }
                Column(
                        Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background),
                    verticalArrangement = Arrangement.Bottom
                ) {


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ShuffleButton(mediaController = mediaController)
                        RepeatButton(mediaController = mediaController)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SeekBar(mediaController = mediaController)
                    }
                }
            }
        }
    )
}

@ExperimentalPagerApi
@Composable
fun ViewPager(mediaController: MediaControllerAdapter,
            pagerState:PagerState = rememberPagerState(initialPage = mediaController.calculateCurrentQueuePosition())) {
    val queue by mediaController.queue.observeAsState(emptyList())

    if (isEmpty(queue)) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Empty Playlist", style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center)
        }
    } else {
        LaunchedEffect(pagerState.currentPage) {
            Log.i("NOW_PLAYING", "current page changed")
            val newPosition = pagerState.currentPage
            val currentQueuePosition = mediaController.calculateCurrentQueuePosition()

            val notAtBeginning = currentQueuePosition > 0
            val notAtEnd = currentQueuePosition < queue.size
            val notCurrentPosition = currentQueuePosition != newPosition

            if (notCurrentPosition ) {
                if (notAtEnd && isSkipToNext(newPosition, currentQueuePosition)) {
                    mediaController.skipToNext()
                } else if (notAtBeginning && isSkipToPrevious(newPosition, currentQueuePosition)) {
                    mediaController.seekTo(0)
                    mediaController.skipToPrevious()
                }
            }
        }

        mediaController.queue.value?.let {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background)
                        .semantics {
                            contentDescription = "viewPagerColumn"
                        },
                count = it.size,
                key = { page : Int ->
                    val queueItem : MediaSessionCompat.QueueItem? = (mediaController.queue.value?.get(page) as MediaSessionCompat.QueueItem)
                    queueItem?.description?.mediaId as Any
                }

            ) { pageIndex ->
            val item: MediaSessionCompat.QueueItem = queue!![pageIndex]
            Column(
                    modifier = Modifier
                            .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                        painter = rememberImagePainter(
                                request =  ImageRequest.Builder(LocalContext.current).data(QueueItemUtils.getAlbumArtUri(item as MediaSessionCompat.QueueItem)).build()
                        ),
                        contentDescription = "Album Art",
                        modifier = Modifier.size(300.dp, 300.dp)
                )
            }
        }
        }
    }

}

private fun isSkipToNext(newPosition: Int, currentPosition : Int): Boolean {
    return newPosition == (currentPosition + 1)
}

private fun isSkipToPrevious(newPosition: Int, currentPosition : Int): Boolean {
    return newPosition == (currentPosition - 1)
}