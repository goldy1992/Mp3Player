package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.ui.components.equalizer.smoothline.SmoothLineEqualizerCanvas
import kotlinx.coroutines.CoroutineScope


@Preview
@Composable
fun SmoothLineCard(
    modifier : Modifier = Modifier,
    frequencyPhases : () -> List<Float> = {  listOf(100f, 200f, 300f, 150f) },
    scope: CoroutineScope = rememberCoroutineScope()
) {
    EqualizerCard(
        modifier = modifier,
        title = "Smooth Line",) { canvasSize, containerModifier ->
        SmoothLineEqualizerCanvas(
            frequencyPhasesState = frequencyPhases,
            canvasSize = canvasSize,
            modifier = containerModifier)
    }

}

