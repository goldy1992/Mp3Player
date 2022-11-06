package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random


@Composable
@Preview
fun Equalizer(maxHeight : Dp = 30.dp,
              numOfBars : Int = 5,
              barColors : List<Color> = listOf(Color.Black),
              backgroundColor : Color = Color.Transparent,
              barWidth : Dp = 10.dp, spaceBetweenBars : Dp = 2.dp) {
    val maxWidth : Dp =  (barWidth.times(numOfBars).plus(spaceBetweenBars.times(numOfBars-1)))
    Column(Modifier.requiredSize(width = maxWidth, height = maxHeight)) {

        val multiColor = barColors.size >= numOfBars

        val list : ArrayList<State<Float>> = ArrayList()
        val maxHeightPx : Float = LocalDensity.current.run { maxHeight.toPx()}
        val barWidthPx : Float = LocalDensity.current.run { barWidth.toPx()}
        val spaceBetweenBarsPx : Float = LocalDensity.current.run { spaceBetweenBars.toPx()}

        for( bar in 1 .. numOfBars) {
            val infiniteTransition = rememberInfiniteTransition()
            val duration = remember {Random.nextInt(400, 600)}
            list.add(infiniteTransition.animateFloat(initialValue = 0f,
                targetValue = maxHeightPx,
                animationSpec = infiniteRepeatable(
                    animation = tween(duration, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )))
        }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)) {
            for ((idx, l) in list.withIndex()) {
                val height = l.value
                val offset : Float = maxHeightPx - height
                drawRect(
                    color = if (multiColor) barColors[idx] else barColors[0],
                    topLeft = Offset((barWidthPx * idx) + (spaceBetweenBarsPx * idx), offset),
                    size = Size(width = barWidthPx, height = height)
                )
            }
        }
    }
}

@Composable
@Preview
fun Equalizer(maxHeight : Dp = 30.dp,
                numOfBars : Int = 5,
                barColor : Color = Color.Black,
                backgroundColor : Color = Color.Transparent,
                barWidth : Dp = 10.dp,
                spaceBetweenBars : Dp = 2.dp) {
    Equalizer(maxHeight =  maxHeight,
    numOfBars = numOfBars,
    barColors = listOf(barColor),
    backgroundColor = backgroundColor,
    barWidth = barWidth,
    spaceBetweenBars = spaceBetweenBars)
}