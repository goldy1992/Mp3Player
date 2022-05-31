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
              numOfBars : Int = 4,
              barColor : Color = MaterialTheme.colorScheme.secondary,
              spaceBetweenBars : Dp = 1.dp) {

    var maxHeight by remember { mutableStateOf(0f)    }

    BoxWithConstraints(modifier = modifier) {
        val bWidth = calculateBarWidth(containerWidth = maxWidth, numOfBars = numOfBars, spaceBetweenBars = spaceBetweenBars)

        val list : ArrayList<State<Float>> = ArrayList()
        val barWidthPx : Float = LocalDensity.current.run { bWidth.toPx()}
        val spaceBetweenBarsPx : Float = LocalDensity.current.run { spaceBetweenBars.toPx()}

        Canvas(modifier = modifier
            .fillMaxSize()
        ) {

            maxHeight = this.size.height
            for ((idx, l) in list.withIndex()) {
                val height = l.value
                val offset : Float = height
                drawRect(
                    color = barColor,
                    topLeft = Offset((barWidthPx * idx) + (spaceBetweenBarsPx * idx), offset),
                    size = Size(width = barWidthPx, height = maxHeight - height)
                )
            }
        }

        for( bar in 1 .. numOfBars) {
            val infiniteTransition = rememberInfiniteTransition()
            val duration = remember {Random.nextInt(400, 600)}
            list.add(infiniteTransition.animateFloat(initialValue = maxHeight,
                targetValue = maxHeight * 0.1f,
                animationSpec = infiniteRepeatable(
                                animation = tween(duration, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                )))
        }
    }
}

