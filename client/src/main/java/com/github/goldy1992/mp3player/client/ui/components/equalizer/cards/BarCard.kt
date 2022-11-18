package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.components.equalizer.bar.BarEqualizerCanvas
import kotlinx.coroutines.CoroutineScope

@Preview
@Composable
fun BarCard(modifier : Modifier = Modifier.width(200.dp).height(200.dp),
            frequencyValues : () -> List<Float> =  {listOf(100f, 200f, 300f, 150f)},
            scope : CoroutineScope = rememberCoroutineScope()
) {
    EqualizerCard(
        modifier = modifier,
        frequencyValues = frequencyValues,
        title = "Bars",
        scope = scope) { frequencies, canvasSize, containerModifier ->
        BarEqualizerCanvas(
            frequencyValues = frequencies,
            canvasSize = canvasSize,
            modifier = containerModifier)
    }
}