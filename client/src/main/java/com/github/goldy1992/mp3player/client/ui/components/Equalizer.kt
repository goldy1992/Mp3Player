package com.github.goldy1992.mp3player.client.ui.components

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.utils.calculateBarWidthPixels
import kotlin.random.Random

const val MAX_AMPLITUDE = 400f

@Preview
@Composable
fun Equalizer(
    modifier: Modifier = Modifier,
    bars : FloatArray = floatArrayOf(),
    barColor : Color = MaterialTheme.colorScheme.secondary,
    spaceBetweenBars : Float = 5f,
    maxHeight : MutableState<Float> = remember { mutableStateOf(0f) },
    maxWidth : MutableState<Float> = remember { mutableStateOf(0f) }) {
    Equalizer(
        modifier = modifier,
        bars = bars.asList(),
        barColor = barColor,
        spaceBetweenBarsPx = spaceBetweenBars
    )
}

@Preview
@Composable
fun Equalizer(
    modifier: Modifier = Modifier,
    bars : List<Float> = emptyList(),
    barColor : Color = MaterialTheme.colorScheme.secondary,
    spaceBetweenBarsPx : Float = 5f,
      ) {


    BoxWithConstraints() {

        val maxHeight : MutableState<Float> = remember { mutableStateOf(0f) }
        val maxWidth : MutableState<Float> = remember { mutableStateOf(0f) }

        val barWidthPx = calculateBarWidthPixels(
            containerWidth = maxWidth.value,
            numOfBars = bars.size,
            spaceBetweenBars = spaceBetweenBarsPx
        )

        //val barWidthPx: Float = LocalDensity.current.run { bWidth.toPx() }


        val equalizerWidthPx : Float = (bars.size * barWidthPx) + (spaceBetweenBarsPx * (bars.size+1))
        val horizontalOffset : Float = ((maxWidth.value - equalizerWidthPx) / 2) + spaceBetweenBarsPx
        Log.i("equalizer", "bar width: ${barWidthPx}")
        Log.i("equalizer", "max width val: ${maxWidth.value}")
        Log.i("equalizer", "equalizer width val: ${equalizerWidthPx}")
        Log.i("equalizer", "Equalizer: horizontal val: $horizontalOffset")
        Canvas(
            modifier = Modifier
                .background(Color.Blue)
                .fillMaxSize()
        ) {

            maxHeight.value = this.size.height
            Log.i("equalizer", "max_height: ${maxHeight.value}")
            maxWidth.value = this.size.width
            for ((idx, bar) in bars.withIndex()) {

                var currentBarHeight = if (bar > MAX_AMPLITUDE) {
                     MAX_AMPLITUDE
                } else {
                    bar
                }
                val barCanvasHeight = (currentBarHeight * maxHeight.value) / MAX_AMPLITUDE
                val offset: Float = maxHeight.value - barCanvasHeight
                drawRect(
                    color = barColor,
                    topLeft = Offset( horizontalOffset + (barWidthPx * idx) + (spaceBetweenBarsPx * idx), offset),
                    size = Size(width = barWidthPx, height = (barCanvasHeight))
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
    spaceBetweenBarsPx : Float = 5f) {

    val list : ArrayList<State<Float>> = ArrayList()


    Equalizer(
        modifier= modifier,
        bars = list.map { v -> v.value },
        barColor  = MaterialTheme.colorScheme.secondary)

    for(bar in 1 .. numOfBars) {
        val infiniteTransition = rememberInfiniteTransition()
        val duration = remember {Random.nextInt(400, 600)}
        list.add(infiniteTransition.animateFloat(initialValue = MAX_AMPLITUDE,
            targetValue = MAX_AMPLITUDE * 0.1f,
            animationSpec = infiniteRepeatable(
                            animation = tween(duration, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
            )))
    }

}

