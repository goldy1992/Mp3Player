package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.utils.NavigationUtils
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.equalizer.VisualizerType
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.BarCard
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.FountainSpringCard
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.SmoothLineCard
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope

private const val LOG_TAG = "VisualizerScreen"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VisualizerCollectionScreen(
    navController : NavController = rememberAnimatedNavController(),
    viewModel: VisualizerCollectionViewModel = viewModel(),
    scope : CoroutineScope = rememberCoroutineScope()) {

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

@Preview
@Composable
fun VisualizerContentCardCollection(
    modifier: Modifier = Modifier,
    audioMagnitudes : () -> List<Float> = { listOf(100f, 200f, 300f, 150f)},
    onClickCard : (VisualizerType) -> Unit = {_->},
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
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .onSizeChanged { gridSizePx = it }) {
        item {
            BarCard(
                modifier = Modifier
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
                modifier = Modifier
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
                modifier = Modifier
                    .width(cardLengthDp)
                    .height(cardLengthDp)
                    .clickable {
                        onClickCard(VisualizerType.FIREWORK)
                    },
                frequencyPhases = audioMagnitudes,
            )
        }
    }

}



@Preview
@Composable
fun TestPadding() {
    Box(modifier = Modifier
        .width(200.dp)
        .height(200.dp)
        .background(Color.Red)) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Green)
                .padding(10.dp)
                ) {

        }
    }
}





