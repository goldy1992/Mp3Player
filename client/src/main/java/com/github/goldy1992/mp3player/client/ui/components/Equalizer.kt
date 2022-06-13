package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.utils.calculateBarWidth
import kotlin.random.Random

@Preview
@Composable
fun Equalizer(
            modifier: Modifier = Modifier,
            bars : List<Float> = emptyList(),
            barColor : Color = MaterialTheme.colorScheme.secondary,
            spaceBetweenBars : Dp = 1.dp,
            maxHeight : MutableState<Float> = remember { mutableStateOf(0f) }) {

    val scale = 1
    BoxWithConstraints(modifier = modifier) {
        val bWidth = calculateBarWidth(
            containerWidth = maxWidth,
            numOfBars = bars.size,
            spaceBetweenBars = spaceBetweenBars
        )

        val barWidthPx: Float = LocalDensity.current.run { bWidth.toPx() }
        val spaceBetweenBarsPx: Float = LocalDensity.current.run { spaceBetweenBars.toPx() }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {

            maxHeight.value = this.size.height
            for ((idx, bar) in bars.withIndex()) {
                val height = bar
                val offset: Float = maxHeight.value - (height * scale)
                drawRect(
                    color = barColor,
                    topLeft = Offset((barWidthPx * idx) + (spaceBetweenBarsPx * idx), offset),
                    size = Size(width = barWidthPx, height = (height *scale))
                )
            }
        }
    }
}

@Preview
@Composable
fun AnimatedEqualizer(
    modifier: Modifier = Modifier,
    numOfBars : Int = 4,
    barColor : Color = MaterialTheme.colorScheme.secondary,
    spaceBetweenBars : Dp = 1.dp) {
    val list : ArrayList<State<Float>> = ArrayList()
    val maxHeight = remember { mutableStateOf(0f) }

    Equalizer(
        modifier= modifier,
        bars = list.map { v -> v.value },
        barColor  = MaterialTheme.colorScheme.secondary,
        spaceBetweenBars  = 1.dp,
        maxHeight = maxHeight)

    for(bar in 1 .. numOfBars) {
        val infiniteTransition = rememberInfiniteTransition()
        val duration = remember {Random.nextInt(400, 600)}
        list.add(infiniteTransition.animateFloat(initialValue = maxHeight.value,
            targetValue = maxHeight.value * 0.1f,
            animationSpec = infiniteRepeatable(
                            animation = tween(duration, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
            )))
    }

}

