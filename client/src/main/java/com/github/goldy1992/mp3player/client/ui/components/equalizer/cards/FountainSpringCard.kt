package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.ui.components.equalizer.fountainspring.FountainSpringEqualizer

@Preview
@Composable
fun FountainSpringCard(
    modifier : Modifier = Modifier,
    frequencyPhases : () -> List<Float> = {  listOf(100f, 200f, 300f, 150f) }
) {
    EqualizerCard(
        modifier = modifier,
        title = "Fountain",
    ) { canvasSize, containerModifier ->
        FountainSpringEqualizer(
            frequencyPhasesProvider = frequencyPhases,
            canvasSize = canvasSize,
            insetPx = canvasSize.heightPx * 0.05f,
            modifier = containerModifier)
    }

}