package com.github.goldy1992.mp3player.client.ui.screens.nowplaying

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import androidx.media3.common.MediaMetadata
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleButton
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.SpeedController
import com.github.goldy1992.mp3player.client.ui.components.seekbar.SeekBar
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.utils.RepeatModeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import org.apache.commons.lang3.ObjectUtils.isEmpty

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@InternalCoroutinesApi
@Composable
fun NowPlayingScreen(
    viewModel: NowPlayingScreenViewModel = viewModel(),
    navController: NavController,
    scope : CoroutineScope = rememberCoroutineScope(),
) {
    val songTitleDescription = stringResource(id = R.string.song_title)
    val metadata by viewModel.metadata.collectAsState()
    val playbackPosition by viewModel.playbackPosition.collectAsState()
    val playbackSpeed by viewModel.playbackSpeed.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val queue by viewModel.queue.collectAsState()
    val shuffleEnabled by viewModel.shuffleMode.collectAsState()
    val repeatMode by viewModel.repeatMode.collectAsState()

    Scaffold (

        topBar = {
            TopAppBar (
                title = {
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
                windowInsets = TopAppBarDefaults.windowInsets
            )
        },
        bottomBar = {
            PlayToolbar(
                isPlayingProvider = { isPlaying },
                onClickPlay = { viewModel.play() },
                onClickPause = { viewModel.pause() },
                onClickSkipPrevious = { viewModel.skipToPrevious() },
                onClickSkipNext = { viewModel.skipToNext() }
            )
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
                SpeedController(
                    playbackSpeedProvider = { playbackSpeed },
                    changePlaybackSpeed = { newSpeed : Float -> viewModel.changePlaybackSpeed(newSpeed)},
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 48.dp, end = 48.dp)
                )
                ViewPager(
                    metadata = { metadata },
                    queueProvider =  {queue },
                    skipToNext = { viewModel.skipToNext()},
                    skipToPrevious = { viewModel.skipToPrevious() },
                    modifier = Modifier.weight(4f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ShuffleButton(
                        shuffleEnabledProvider = { shuffleEnabled },
                        onClick = { isEnabled -> viewModel.setShuffleMode(!isEnabled) }
                    )
                    RepeatButton(
                        repeatModeProvider = { repeatMode },
                        onClick = { currentRepeatMode -> viewModel.setRepeatMode(RepeatModeUtils.getNextRepeatMode(currentRepeatMode)) }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SeekBar(
                        playbackSpeedProvider = { playbackSpeed },
                        metadataProvider = {  metadata },
                        isPlayingProvider = { isPlaying },
                        playbackPositionProvider = {  playbackPosition },
                        seekTo = { value -> viewModel.seekTo(value)})
                }
            }

        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPager(metadata : () -> MediaMetadata,
              queueProvider: () -> QueueState,
              skipToNext : () -> Unit,
              skipToPrevious : () -> Unit,
              modifier: Modifier = Modifier,
              pagerState: PagerState = androidx.compose.foundation.pager.rememberPagerState(
                  initialPage = queueProvider().currentIndex
              ),
           ) {
    val queueState = queueProvider()
    val numberOfPages = queueState.items.size
    val currentQueuePosition = queueState.currentIndex

    if (isEmpty(queueState.items)) {
        Column(modifier = modifier.width(700.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Empty Playlist", style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center)
        }
    } else {
        LaunchedEffect(metadata()){
            if (currentQueuePosition < numberOfPages) {
                pagerState.scrollToPage(currentQueuePosition)
            }
        }

        LaunchedEffect(pagerState.currentPage) {
            Log.i("NOW_PLAYING", "current page changed: ${pagerState.currentPage}")
            val newPosition = pagerState.currentPage
            val atBeginning = currentQueuePosition <= 0
            val atEnd = (currentQueuePosition + 1) >= queueState.items.size
            val atCurrentPosition = currentQueuePosition == newPosition

            if (!atCurrentPosition ) {
                if (!atEnd && isSkipToNext(newPosition, currentQueuePosition)) {
                    skipToNext()
                } else if (!atBeginning && isSkipToPrevious(newPosition, currentQueuePosition)) {
                    skipToPrevious()
                }
            }
        }
        Log.i("NOW_PLAYING_SCRN", "queue size: $numberOfPages")

        HorizontalPager(
                state = pagerState,
                modifier = modifier
                    .width(400.dp)
                    .semantics {
                        contentDescription = "viewPagerColumn"
                    },
                pageCount = numberOfPages,
                key = { page : Int -> queueState.items[page].mediaId }

            ) { pageIndex ->
            val item: MediaItem = queueState.items[pageIndex]
            Column(
                    modifier = Modifier
                            .width(300.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current).data(item.mediaMetadata.artworkUri).build()
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