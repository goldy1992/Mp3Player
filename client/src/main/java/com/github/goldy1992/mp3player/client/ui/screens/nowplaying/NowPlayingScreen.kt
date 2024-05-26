package com.github.goldy1992.mp3player.client.ui.screens.nowplaying

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.ui.buttons.RepeatButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToPreviousButton
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.SpeedController
import com.github.goldy1992.mp3player.client.ui.components.ViewPager
import com.github.goldy1992.mp3player.client.ui.components.seekbar.SeekBar
import com.github.goldy1992.mp3player.client.utils.RepeatModeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi

const val LOG_TAG = "NowPlayingScreen"

@Composable
fun SharedTransitionScope.NowPlayingScreen(
    viewModel: NowPlayingScreenViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    scope : CoroutineScope = rememberCoroutineScope(),
    animatedContentScope: AnimatedContentScope
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
                    queue = queue,
                    skipToNext = { viewModel.skipToNext() },
                    skipToPrevious = { viewModel.skipToPrevious() },
                    modifier = Modifier.Companion
                        .sharedElement(
                            rememberSharedContentState(key = currentSong.id),
                            animatedVisibilityScope = animatedContentScope
                        )
                        .weight(4f)
                    )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 2.dp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ShuffleButton(
                        shuffleEnabledProvider = { shuffleEnabled },
                        onClick = { isEnabled -> viewModel.setShuffleEnabled(isEnabled) }
                    )
                    SkipToPreviousButton{
                        viewModel.skipToPrevious()
                    }
                    PlayPauseButton(
                        modifier = Modifier.size(60.dp),//.border(BorderStroke(1.dp, Color.Red)),
                        isPlaying = {isPlaying},
                        onClickPlay = { viewModel.play() },
                        onClickPause = { viewModel.pause() }
                    )
                    SkipToNextButton {
                        viewModel.skipToNext()
                    }
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


