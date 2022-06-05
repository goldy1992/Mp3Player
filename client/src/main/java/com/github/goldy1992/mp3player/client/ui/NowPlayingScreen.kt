package com.github.goldy1992.mp3player.client.ui

import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
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
import coil.annotation.ExperimentalCoilApi
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

@OptIn(ExperimentalMaterial3Api::class)
@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun NowPlayingScreen(
    navController: NavController,
    mediaController: MediaControllerAdapter,
    scope : CoroutineScope = rememberCoroutineScope(),
) {
    val songTitleDescription = stringResource(id = R.string.song_title)
    Scaffold (

        topBar = {
            SmallTopAppBar (
                title = {
                    val metadata by mediaController.metadata.observeAsState()
                    val title : String = metadata?.description?.title.toString()
                    val artist : String = metadata?.description?.subtitle.toString()
                    Column {
                        Text(text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.semantics {
                                contentDescription = songTitleDescription
                            }
                            )
                        Text(text = artist,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface)
                    }
                },

                navigationIcon = {
                    NavUpButton(
                        navController = navController,
                        scope = scope)
                },
                actions = {},

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
                    .padding(it)
                    .background(MaterialTheme.colorScheme.surface)
                    .semantics {
                        contentDescription = nowPlayingDescr
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpeedController(mediaController = mediaController,
                    modifier = Modifier.weight(1f)
                        .padding(start = 48.dp, end = 48.dp)
                )
                ViewPager(mediaController = mediaController,
                modifier = Modifier.weight(4f))

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ShuffleButton(mediaController = mediaController)
                    RepeatButton(mediaController = mediaController)
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SeekBar(mediaController = mediaController)
                }
            }

        }
    )
}

@OptIn(ExperimentalCoilApi::class, ExperimentalPagerApi::class)
@Composable
fun ViewPager(mediaController: MediaControllerAdapter,
              modifier: Modifier = Modifier,
            pagerState:PagerState = rememberPagerState(initialPage = mediaController.calculateCurrentQueuePosition())
           ) {
    val queue by mediaController.queue.observeAsState(emptyList())
    val metadata by mediaController.metadata.observeAsState()
    val currentQueuePosition = mediaController.calculateCurrentQueuePosition()

    if (isEmpty(queue)) {
        Column(modifier = modifier.width(700.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Empty Playlist", style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center)
        }
    } else {
        LaunchedEffect(metadata){
            if (currentQueuePosition < pagerState.pageCount) {
                pagerState.scrollToPage(currentQueuePosition)
            }
        }

        LaunchedEffect(pagerState.currentPage) {
            Log.i("NOW_PLAYING", "current page changed: ${pagerState.currentPage}")
            val newPosition = pagerState.currentPage
            val atBeginning = currentQueuePosition <= 0
            val atEnd = (currentQueuePosition + 1) >= queue.size
            val atCurrentPosition = currentQueuePosition == newPosition

            if (!atCurrentPosition ) {
                if (!atEnd && isSkipToNext(newPosition, currentQueuePosition)) {
                    mediaController.skipToNext()
                } else if (!atBeginning && isSkipToPrevious(newPosition, currentQueuePosition)) {
                    mediaController.seekTo(0)
                    mediaController.skipToPrevious()
                }
            }
        }
        Log.i("NOW_PLAYING_SCRN", "queue size: ${queue?.size ?: 0}")

        HorizontalPager(
                state = pagerState,
                modifier = modifier.width(400.dp)
                    .semantics {
                        contentDescription = "viewPagerColumn"
                    },
                count = queue?.size ?: 0 ,
                key = { page : Int ->
                    val queueItem : MediaSessionCompat.QueueItem? = (mediaController.queue.value?.get(page) as MediaSessionCompat.QueueItem)
                    queueItem?.description?.mediaId as Any
                }

            ) { pageIndex ->
            val item: MediaSessionCompat.QueueItem = queue!![pageIndex]
            Column(
                    modifier = Modifier
                            .width(300.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                        painter = rememberImagePainter(
                                request =  ImageRequest.Builder(LocalContext.current).data(QueueItemUtils.getAlbumArtUri(item)).build()
                        ),
                        contentDescription = "Album Art",
                        modifier = Modifier.size(300.dp, 300.dp)
                )
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