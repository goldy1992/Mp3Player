package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.components.equalizer.EqualizerContainer
import com.github.goldy1992.mp3player.client.ui.screens.DpPxSize
import kotlinx.coroutines.CoroutineScope

@Preview
@Composable
fun EqualizerCard(
    modifier: Modifier = Modifier,
    title: String = "Title",
    density: Density = LocalDensity.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    frequencyValues: () -> List<Float> =  {listOf(100f, 200f, 300f, 150f)},
    initialCanvasSizeDpPx: DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, density),
    initialPaddingDpPx: DpPxSize = DpPxSize.createDpPxSizeFromDp(10.dp, 10.dp, density),
    equalizer: @Composable (
        frequencyValues : () -> List<Float>,
        canvasSize : DpPxSize,
        modifier: Modifier) -> Unit = { _, _, _ -> }
) {
    var size = initialCanvasSizeDpPx
    val padding = initialPaddingDpPx

    Card(border = BorderStroke(2.dp, Color.Red),
        modifier = modifier
            .width(size.widthDp)
            .height(size.heightDp)
        ,
        elevation = CardDefaults.outlinedCardElevation()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(all = padding.widthDp)) {

            var equalizerSize by remember {
                mutableStateOf(
                    DpPxSize.createDpPxSizeFromPx(
                        size.widthPx - (padding.widthPx * 2),
                        (size.heightPx - (padding.widthPx * 2)) * (5f / 7f),
                        density
                    )
                )
            }

            Row(
                Modifier
                    .padding(horizontal = padding.widthDp)
                    .weight(8f)
                    .onSizeChanged {
                        equalizerSize = DpPxSize.createDpPxSizeFromPx(
                            it.width.toFloat() - padding.widthPx,
                            it.height.toFloat() - padding.widthPx,
                            density
                        )
                    }) {
                EqualizerContainer(  modifier = modifier,
                    frequencyValues  = frequencyValues,
                    scope = scope,
                    containerSize = equalizerSize,
                ) { frequencies, canvasSize, containerModifier ->
                    equalizer(frequencyValues = frequencies,
                        canvasSize = canvasSize,
                        modifier = containerModifier)
                }

            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(2f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = title)
            }
        }
    }

}
