package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.equalizer.pichart.PieChartVisualizer

@Preview
@Composable
fun PieChartCard(
    modifier : Modifier = Modifier,
    frequencyPhases : () -> List<Float> = {  listOf(100f, 200f, 300f, 150f) }
) {
    EqualizerCard(
        modifier = modifier,
        title = stringResource(id = R.string.pie_chart),
    ) { canvasSize, containerModifier ->
        PieChartVisualizer(
            frequencyPhasesState = frequencyPhases,
            canvasSize = canvasSize,
            modifier = containerModifier
        )
    }

}