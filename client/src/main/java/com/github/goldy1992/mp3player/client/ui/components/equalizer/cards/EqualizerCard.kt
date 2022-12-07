package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.DpPxSize

@Preview
@Composable
fun EqualizerCard(
    modifier: Modifier = Modifier,
    title: String = "Title",
    density: Density = LocalDensity.current,
    equalizer: @Composable (
        canvasSize : DpPxSize,
        modifier: Modifier) -> Unit = { _, _-> }
) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.outlinedCardElevation()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            var equalizerSize by remember {
                mutableStateOf(

                    DpPxSize.createDpPxSizeFromPx(
                        0f, 0f, density
                    )
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(8f)
                    .onSizeChanged {
                        equalizerSize = DpPxSize.createDpPxSizeFromPx(
                            it.width.toFloat(),
                            it.height.toFloat(),
                            density
                        )
                    }) {
                    equalizer(
                        canvasSize = equalizerSize,
                        modifier = Modifier.fillMaxSize()
                    )
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

