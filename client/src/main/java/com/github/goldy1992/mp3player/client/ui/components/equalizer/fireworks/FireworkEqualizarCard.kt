package com.github.goldy1992.mp3player.client.ui.components.equalizer.fireworks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.EqualizerCard
import kotlinx.coroutines.CoroutineScope


@RequiresApi(Build.VERSION_CODES.N)
@Preview
@Composable
fun FireworkEqualizerCard(
    modifier : Modifier = Modifier,
    frequencyPhases : () -> List<Float> = {  listOf(100f, 200f, 300f, 150f) },
    isPlayingProvider : () -> Boolean = { false },
    scope: CoroutineScope = rememberCoroutineScope()
) {
    EqualizerCard(
        modifier = modifier,
        title = "Fireworks",
    ) { canvasSize, containerModifier ->
        FireworkWrapperNew(
            frequencyPhasesProvider = frequencyPhases,
            canvasSize = canvasSize,
            modifier = containerModifier)
    }

}