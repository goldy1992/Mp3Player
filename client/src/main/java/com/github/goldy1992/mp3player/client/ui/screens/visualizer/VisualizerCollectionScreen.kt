package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.equalizer.VisualizerType
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.BarCard
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.CircularEqualizerCard
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.FountainSpringCard
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.PieChartCard
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.SmoothLineCard
import com.github.goldy1992.mp3player.client.utils.NavigationUtils
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope

private const val LOG_TAG = "VisualizerScreen"

@Composable
fun SharedTransitionScope.VisualizerCollectionScreen(
    navController : NavController = rememberNavController(),
    viewModel: VisualizerCollectionViewModel = viewModel(),
    scope : CoroutineScope = rememberCoroutineScope(),
    animatedContentScope: AnimatedContentScope
) {

    val audioMagnitudes by viewModel.audioData.state().collectAsState()
    val isPlaying by viewModel.isPlaying.state().collectAsState()
    val onClickCard : (VisualizerType) -> Unit = { NavigationUtils.navigate(navController, it) }

    Scaffold(
        topBar = {
            TopBar {
                NavUpButton(
                    navController = navController,
                    scope = scope
                )
            }
         },
        bottomBar = {
            PlayToolbar(
                animatedVisibilityScope = animatedContentScope,
                isPlayingProvider = { isPlaying },
                onClickPlay = { viewModel.play() },
                onClickPause = {viewModel.pause() },
                onClickSkipPrevious = { viewModel.skipToPrevious() },
                onClickSkipNext = { viewModel.skipToNext() },
                onClickBar = { navController.navigate(Screen.NOW_PLAYING.name)}
            )
        },
    ) {
        VisualizerContentCardCollection(
            modifier = Modifier.padding(it),
            audioMagnitudes = {audioMagnitudes},
            isPlaying = { isPlaying },
            animatedContentScope = animatedContentScope,
            onClickCard = onClickCard
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopBar(navIcon: @Composable () -> Unit = {}) {
    val titleText = stringResource(id = R.string.visualizer)
    TopAppBar(
       title = {
           Text(text = titleText,
               style = MaterialTheme.typography.titleLarge,
               color = MaterialTheme.colorScheme.onSurface,
               modifier = Modifier.semantics {
                contentDescription = titleText
               }
           )

        },
        navigationIcon = navIcon,
        actions = {},
        windowInsets = TopAppBarDefaults.windowInsets
    )
}

@Composable
fun SharedTransitionScope.VisualizerContentCardCollection(
    modifier: Modifier = Modifier,
    audioMagnitudes : () -> List<Float> = { listOf(100f, 200f, 300f, 150f)},
    isPlaying : () -> Boolean = {false},
    onClickCard : (VisualizerType) -> Unit = {_->},
    animatedContentScope: AnimatedContentScope,
    scope: CoroutineScope = rememberCoroutineScope()) {

    val density = LocalDensity.current
    var gridSizePx = remember { IntSize(400, 400) }
    val cardLengthDp = remember(gridSizePx) {
        var lengthDp: Dp
        with (density) {
            lengthDp = gridSizePx.width.toDp()
        }
        lengthDp
  }
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .onSizeChanged { gridSizePx = it }) {
        item {
            BarCard(
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(VisualizerType.BAR),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .skipToLookaheadSize()
                    .width(cardLengthDp)
                    .height(cardLengthDp)
                    .clickable {
                       onClickCard(VisualizerType.BAR)
                    },
                frequencyValues = audioMagnitudes,
            )
        }
        item {
            SmoothLineCard(
                modifier = Modifier  .sharedElement(
                    rememberSharedContentState(VisualizerType.LINE),
                    animatedVisibilityScope = animatedContentScope
                )
                    .width(cardLengthDp)
                    .height(cardLengthDp)
                    .clickable {
                        onClickCard(VisualizerType.LINE)
                    },
                frequencyPhases = audioMagnitudes,
            )
        }

        item {
            FountainSpringCard(
                modifier = Modifier  .sharedElement(
                    rememberSharedContentState(VisualizerType.FOUNTAIN),
                    animatedVisibilityScope = animatedContentScope
                )
                    .width(cardLengthDp)
                    .height(cardLengthDp)
                    .clickable {
                        onClickCard(VisualizerType.FOUNTAIN)
                    },
                isPlaying = isPlaying,
                frequencyPhases = audioMagnitudes,
            )
        }

        item {
            CircularEqualizerCard(
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(VisualizerType.CIRCULAR),
                    animatedVisibilityScope = animatedContentScope
                )
                    .width(cardLengthDp)
                    .height(cardLengthDp)
                    .clickable {
                        onClickCard(VisualizerType.CIRCULAR)
                    },
                frequencyPhases = audioMagnitudes,
            )
        }

        item {
            PieChartCard(
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(VisualizerType.PIE_CHART),
                    animatedVisibilityScope = animatedContentScope
                )
                    .width(cardLengthDp)
                    .height(cardLengthDp)
                    .clickable {
                        onClickCard(VisualizerType.PIE_CHART)
                    },
                frequencyPhases = audioMagnitudes,
            )
        }
    }

}






