package com.github.goldy1992.mp3player.client.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.AsyncPlayerListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleButton
import com.github.goldy1992.mp3player.client.viewmodels.LibraryScreenViewModel
import com.github.goldy1992.mp3player.client.viewmodels.NowPlayingScreenViewModel
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
    viewModel: NowPlayingScreenViewModel = viewModel(),
    navController: NavController,
    scope : CoroutineScope = rememberCoroutineScope(),
) {
    val songTitleDescription = stringResource(id = R.string.song_title)
    Scaffold (

        topBar = {
            SmallTopAppBar (
                title = {
                    val metadata by viewModel.asyncPlayerListener.mediaMetadataState.collectAsState()
                    val title : String = metadata.title.toString()
                    val artist : String = metadata.artist.toString()
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
            PlayToolbar(mediaController = viewModel.mediaControllerAdapter,
                        asyncPlayerListener = viewModel.asyncPlayerListener,
                        scope = scope) {
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
                SpeedController(mediaController = viewModel.mediaControllerAdapter,
                    asyncPlayerListener = viewModel.asyncPlayerListener,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 48.dp, end = 48.dp)
                )
                ViewPager(mediaController = viewModel.mediaControllerAdapter,
                    playerListener = viewModel.asyncPlayerListener,
                scope = scope,
                modifier = Modifier.weight(4f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ShuffleButton(mediaController = viewModel.mediaControllerAdapter, asyncPlayerListener = viewModel.asyncPlayerListener, scope = scope)
                    RepeatButton(mediaController = viewModel.mediaControllerAdapter, asyncPlayerListener = viewModel.asyncPlayerListener, scope = scope)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SeekBar(mediaController = viewModel.mediaControllerAdapter,
                    asyncPlayerListener = viewModel.asyncPlayerListener)
                }
            }

        }
    )
}

@ExperimentalPagerApi
@Composable
fun ViewPager(mediaController: MediaControllerAdapter,
              playerListener: AsyncPlayerListener,
              modifier: Modifier = Modifier,
            pagerState:PagerState = rememberPagerState(initialPage = mediaController.getCurrentQueuePosition()),
            scope: CoroutineScope = rememberCoroutineScope()
           ) {
    val queue by playerListener.queueState.collectAsState()
    val metadata by playerListener.mediaMetadataState.collectAsState()
    val currentQueuePosition = mediaController.getCurrentQueuePosition()

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
                modifier = modifier
                    .width(400.dp)
                    .semantics {
                        contentDescription = "viewPagerColumn"
                    },
                count = queue.size  ,
                key = { page : Int -> queue[page].mediaId }

            ) { pageIndex ->
            val item: MediaItem = queue[pageIndex]
            Column(
                    modifier = Modifier
                            .width(300.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                        painter = rememberImagePainter(
                                request =  ImageRequest.Builder(LocalContext.current).data(item.mediaMetadata.artworkUri).build()
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