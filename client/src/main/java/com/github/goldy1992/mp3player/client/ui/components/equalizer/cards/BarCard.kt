package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.ui.components.equalizer.bar.BarEqualizer
import kotlinx.coroutines.CoroutineScope

@Preview(name = "Bar Card",
widthDp = 200,
heightDp = 200)
@Composable
fun BarCard(modifier : Modifier = Modifier,
            frequencyValues : () -> List<Float> =  {listOf(100f, 200f, 300f, 150f)},
            scope : CoroutineScope = rememberCoroutineScope()
) {
    EqualizerCard(
        modifier = modifier,
        title = "Bars") { canvasSize, containerModifier ->

        BarEqualizer(
            canvasSize = canvasSize,
            frequencyValues = frequencyValues,
            modifier = containerModifier,
            scope = scope
        )
    }
}