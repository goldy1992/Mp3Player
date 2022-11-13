package com.github.goldy1992.mp3player.client.ui.components.equalizer

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.utils.calculateBarWidthPixels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val MAX_AMPLITUDE = 400f
private const val logTag = "BarEqualizer"

@Preview
@Composable
fun BarEqualizer(
    modifier: Modifier = Modifier,
    bars : FloatArray = floatArrayOf(),
    barColor : Color = MaterialTheme.colorScheme.secondary,
    spaceBetweenBars : Float = 5f) {
    BarEqualizer(
        modifier = modifier.fillMaxSize(),
        bars = bars.asList(),
        barColor = barColor,
        spaceBetweenBarsPx = spaceBetweenBars
    )
}

@Preview
@Composable
fun BarEqualizer(
    modifier: Modifier = Modifier,
    bars : List<Float> = emptyList(),
    barColor : Color = MaterialTheme.colorScheme.secondary,
    spaceBetweenBarsPx : Float = 5f,
    scope : CoroutineScope = rememberCoroutineScope()
) {
    var maxHeight by remember { mutableStateOf(0f) }
    var maxWidth by remember { mutableStateOf(0f) }


    val barWidthPx = remember(bars.size, maxWidth, spaceBetweenBarsPx) {
        calculateBarWidthPixels(
            containerWidth = maxWidth,
            numOfBars = bars.size,
            spaceBetweenBars = spaceBetweenBarsPx
        )
    }

    val equalizerWidthPx = remember(bars.size, barWidthPx, spaceBetweenBarsPx) { (bars.size * barWidthPx) + (spaceBetweenBarsPx * (bars.size+1)) }

    val barStates : SnapshotStateList<Animatable<Float, AnimationVector1D>> = remember(bars.size) {
        mutableStateListOf<Animatable<Float, AnimationVector1D>>().apply {
            Log.i(logTag, "retrigger remember")
            for (i in bars) add( Animatable(i)) }
    }

    LaunchedEffect(bars) {
        Log.i(logTag, "Triggered launch effect")
        for (i in bars.indices) {
            scope.launch { barStates[i].animateTo(targetValue = bars[i], animationSpec = tween(300)) }
        }
    }

    val horizontalOffset = remember(maxWidth, equalizerWidthPx, spaceBetweenBarsPx) { ((maxWidth - equalizerWidthPx) / 2) + spaceBetweenBarsPx }
//        Log.i("equalizer", "bar width: ${barWidthPx}")
//        Log.i("equalizer", "max width val: ${maxWidth}")
//        Log.i("equalizer", "equalizer width val: ${equalizerWidthPx}")
//        Log.i("equalizer", "Equalizer: horizontal val: $horizontalOffset")
    Canvas(
        modifier = modifier
            .onSizeChanged {
                maxHeight = it.height.toFloat()
                maxWidth = it.width.toFloat()
            }
            .fillMaxSize()

    ) {

        for ((idx, bar) in barStates.withIndex()) {

            val currentBarHeight = if (bar.value > MAX_AMPLITUDE) {
                MAX_AMPLITUDE
            } else {
                bar.value
            }
            val barCanvasHeight = (currentBarHeight * maxHeight) / MAX_AMPLITUDE
            val offset: Float = maxHeight - barCanvasHeight
            drawRect(
                color = barColor,
                topLeft = Offset( horizontalOffset + (barWidthPx * idx) + (spaceBetweenBarsPx * idx), offset),
                size = Size(width = barWidthPx, height = (barCanvasHeight))
            )
        }
    }

}

@Preview
@Composable
fun AnimatedBarEqualizer(
    modifier: Modifier = Modifier,
    numOfBars : Int = 4,
    barColor : Color = MaterialTheme.colorScheme.secondary,
    spaceBetweenBarsPx : Float = 5f) {
    Log.i("equalizer", "recomposing")
    val list : ArrayList<State<Float>> = ArrayList()

    for(bar in 1 .. numOfBars) {
        val infiniteTransition = rememberInfiniteTransition()
        val duration = remember {Random.nextInt(400, 600)}
        val state: State<Float> = infiniteTransition.animateFloat(
            initialValue = MAX_AMPLITUDE,
            targetValue = MAX_AMPLITUDE * 0.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        list.add(state)
    }
    BarEqualizer(
        modifier= modifier,
        bars = list.map { v -> v.value },
        barColor  = barColor,
        spaceBetweenBarsPx = spaceBetweenBarsPx,
    )

}

