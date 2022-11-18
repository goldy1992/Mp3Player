package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.components.equalizer.BarEqualizerCanvas
import com.github.goldy1992.mp3player.client.ui.screens.DpPxSize
import kotlinx.coroutines.CoroutineScope

@Preview
@Composable
fun BarCard(modifier : Modifier = Modifier,
            frequencyValues : () -> List<Float> =  {listOf(100f, 200f, 300f, 150f)},
            scope : CoroutineScope = rememberCoroutineScope(),
            cardSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
            initialPaddingDpPx : DpPxSize = DpPxSize.createDpPxSizeFromDp(10.dp, 10.dp, LocalDensity.current),

) {
    EqualizerCard(
        modifier = modifier,
                    title = "Bars",
        initialCanvasSizeDpPx = cardSize,
        initialPaddingDpPx = initialPaddingDpPx,
        scope = scope) { frequencies, canvasSize, containerModifier ->
        BarEqualizerCanvas(
            frequencyValues = frequencies,
            canvasSize = canvasSize,
            modifier = containerModifier)
    }
}