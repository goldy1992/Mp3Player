package com.github.goldy1992.mp3player.client.ui.components

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.utils.calculateBarWidth
import kotlin.random.Random

private const val LOG_TAG = "Equalizer"

@Preview
@Composable
fun Equalizer(
            modifier: Modifier = Modifier,
              numOfBars : Int = 5,
              barColors : List<Color> = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)),
              backgroundColor : Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
              barWidth : Dp = 10.dp,
            spaceBetweenBars : Dp = 1.dp) {

    var maxHeight by remember { mutableStateOf(0f)    }

    BoxWithConstraints() {
        val bWidth = calculateBarWidth(containerWidth = maxWidth, numOfBars = numOfBars, spaceBetweenBars = spaceBetweenBars)

        val multiColor = barColors.size >= numOfBars

        val list : ArrayList<State<Float>> = ArrayList()
        val barWidthPx : Float = LocalDensity.current.run { bWidth.toPx()}
        val spaceBetweenBarsPx : Float = LocalDensity.current.run { spaceBetweenBars.toPx()}

        Canvas(modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
        ) {

            maxHeight = this.size.height
            Log.i(LOG_TAG, "canvas maxHeight: ${maxHeight}")

            for ((idx, l) in list.withIndex()) {
                val height = l.value
                val offset : Float = height
                drawRect(
                    color = if (multiColor) barColors[idx] else barColors[0],
                    topLeft = Offset((barWidthPx * idx) + (spaceBetweenBarsPx * idx), offset),
                    size = Size(width = barWidthPx, height = maxHeight - height)
                )
                if (idx == 0) {
                    Log.d("Equalizer", "Equalizer: topLeftHeight: $offset bottomHeight: mh(${maxHeight})-offset(${offset} = ${maxHeight - offset})" )
                }
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

@Composable
@Preview
fun EqualizerPreview() {
    Equalizer(modifier = Modifier.size(40.dp),
    numOfBars = 3,
    barColors = listOf(Color.Red),
    //backgroundColor = backgroundColor,
    //barWidth = barWidth,
    spaceBetweenBars = 2.dp)
}

