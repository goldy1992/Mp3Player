package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.equalizer.bar.BarEqualizer

@Composable
fun SharedTransitionScope.BarCard(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier : Modifier = Modifier,

    frequencyValues : () -> List<Float> =  {listOf(100f, 200f, 300f, 150f)},
) {
    EqualizerCard(
        modifier = modifier,
        title = stringResource(id = R.string.bars)) { canvasSize, containerModifier ->

        BarEqualizer(
            canvasSize = canvasSize,
            frequencyValues = frequencyValues,
            modifier = containerModifier,
            animatedVisibilityScope = animatedVisibilityScope

        )
    }
}