package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
    equalizer: @Composable (
        frequencyValues : () -> List<Float>,
        canvasSize : DpPxSize,
        modifier: Modifier) -> Unit = { _, _, _ -> }
) {

    Card(border = BorderStroke(2.dp, Color.Red),
        modifier = modifier,
        elevation = CardDefaults.outlinedCardElevation()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ){

            var equalizerSize by remember {
                mutableStateOf(

                    DpPxSize.createDpPxSizeFromPx(
                        0f, 0f, density
                    )
                )
            }

            Row(
                Modifier
                    .weight(8f)
                    .fillMaxSize()
                    .onSizeChanged {
                        equalizerSize = DpPxSize.createDpPxSizeFromPx(
                            it.width.toFloat(),
                            it.height.toFloat(),
                            density
                        )
                    }) {
                EqualizerContainer(
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
                    .weight(2f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = title)
            }
        }
    }

}
