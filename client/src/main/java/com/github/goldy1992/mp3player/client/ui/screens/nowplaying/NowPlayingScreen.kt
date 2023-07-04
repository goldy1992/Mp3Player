package com.github.goldy1992.mp3player.client.ui.screens.nowplaying

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.Song
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleButton
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.SpeedController
import com.github.goldy1992.mp3player.client.ui.components.seekbar.SeekBar
import com.github.goldy1992.mp3player.client.models.Queue
import com.github.goldy1992.mp3player.client.utils.RepeatModeUtils
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import org.apache.commons.lang3.ObjectUtils.isEmpty

const val LOG_TAG = "NowPlayingScreen"
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@InternalCoroutinesApi
@Composable
fun NowPlayingScreen(
    viewModel: NowPlayingScreenViewModel = viewModel(),
    navController: NavController = rememberAnimatedNavController(),
    scope : CoroutineScope = rememberCoroutineScope(),
) {
    val songTitleDescription = stringResource(id = R.string.song_title)
    val playbackPosition by viewModel.playbackPosition.state().collectAsState()
    val playbackSpeed by viewModel.playbackSpeed.state().collectAsState()
    val isPlaying by viewModel.isPlaying.state().collectAsState()
    val queue by viewModel.queue.state().collectAsState()
    val shuffleEnabled by viewModel.shuffleMode.state().collectAsState()
    val repeatMode by viewModel.repeatMode.state().collectAsState()
    val currentSong by viewModel.currentSong.state().collectAsState()

    Scaffold (

        topBar = {
            TopAppBar (
                title = {
                    val title : String = currentSong.title
                    val artist : String = currentSong.artist
                    Column {
                        Text(text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.semantics {
                                contentDescription = songTitleDescription
                            }
                            )
                        Text(text = artist,
                            overflow = TextOverflow.Ellipsis,
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
                onClickSkipNext = { viewModel.skipToNext() },
                currentSongProvider = { currentSong }
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
                    currentSongProvider = { currentSong },
                    queueProvider =  {queue },
                    skipToNext = { viewModel.skipToNext()},
                    skipToPrevious = { viewModel.skipToPrevious() },
                    modifier = Modifier.weight(4f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ShuffleButton(
                        shuffleEnabledProvider = { shuffleEnabled },
                        onClick = { isEnabled -> viewModel.setShuffleEnabled(isEnabled) }
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
                        currentSongProvider = {  currentSong },
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
fun ViewPager(currentSongProvider : () -> Song,
              queueProvider: () -> Queue,
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Empty Playlist", style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center)
        }
    } else {
        LaunchedEffect(currentSongProvider()){
            if (currentQueuePosition < numberOfPages) {
                pagerState.scrollToPage(currentQueuePosition)
            }
        }

        LaunchedEffect(pagerState.currentPage) {
            Log.v(LOG_TAG, "ViewPager() LaunchedEffect: current page changed: ${pagerState.currentPage}")
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
        Log.v(LOG_TAG, "ViewPager() queue size: $numberOfPages")

        HorizontalPager(
                state = pagerState,
                modifier = modifier
                    .width(400.dp)
                    .semantics {
                        contentDescription = "viewPagerColumn"
                    },
                pageCount = numberOfPages,
                key = { page : Int -> queueState.items[page].id }

            ) { pageIndex ->
            val item: Song = queueState.items[pageIndex]
            Column(
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current).data(item.albumArt).build()
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