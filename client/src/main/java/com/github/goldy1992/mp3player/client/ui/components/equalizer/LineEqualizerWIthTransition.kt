package com.github.goldy1992.mp3player.client.ui.components.equalizer

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

private const val AMPLITUDE = 50f

private const val logTag = "LineEqualizerWithTransition"


private var fp : List<Float> = emptyList()

@Composable
@Preview
fun LineEqualizerWithTransition(modifier: Modifier = Modifier,
                  frequencyPhases : List<Float> = emptyList(),
                  insetPx : Float = 200f) {

    if (frequencyPhases == fp) {
        Log.i(logTag, "recomposition")
    } else {
        fp = frequencyPhases
        Log.i(logTag, "new data")
    }

    val currentValues = remember(frequencyPhases.size) {

        mutableStateListOf<Float>().apply {
            Log.i(logTag, "retrigger remember")
            for (i in frequencyPhases) add(
                i) } }
    
    val transition = updateTransition(targetState = true, label = "equalizer-transition")

    for (i in frequencyPhases.indices) {
        val x by transition.animateFloat(label = "animate $i",
        transitionSpec =  { tween(30000, easing = LinearEasing)}) {
            if (it) frequencyPhases[i] else 0f  }
        currentValues[i] = x
    }

    BoxWithConstraints {
        val numberOfPhases : Int = frequencyPhases.size
        val maxHeight : MutableState<Float> = remember { mutableStateOf(0f) }
        val maxWidth : MutableState<Float> = remember { mutableStateOf(0f) }
        val phaseSpacing : Float = (maxWidth.value - (2 * insetPx ) ) / (numberOfPhases + 1)
        val lineHeight = this.maxHeight.value / 2
        var currentOffset  = Offset(0f, lineHeight)
        Canvas(modifier = modifier
            .fillMaxSize()) {
            maxHeight.value = size.height
            maxWidth.value = size.width

            var newOffset = Offset(currentOffset.x + insetPx, lineHeight)
            drawLine(
                start = currentOffset,
                end = newOffset,
                color = Color.Blue,
                strokeWidth = 10f
            )
            currentOffset = newOffset
            for (i in currentValues.indices) {
                newOffset = Offset(currentOffset.x + phaseSpacing, lineHeight + (currentValues[i] * AMPLITUDE))
                drawLine(
                    start = currentOffset,
                    end = newOffset,
                    color = Color.Blue,
                    strokeWidth = 10f
                )
                currentOffset = newOffset
            }

            newOffset = Offset(currentOffset.x + phaseSpacing, lineHeight)
            drawLine(
                start = currentOffset,
                end = newOffset,
                color = Color.Blue,
                strokeWidth = 10f
            )
            currentOffset = newOffset

            drawLine(
                start = currentOffset,
                end = Offset(currentOffset.x + insetPx, lineHeight),
                color = Color.Blue,
                strokeWidth = 10f
            )

            drawLine(
                start = currentOffset,
                end = Offset(currentOffset.x + insetPx, lineHeight),
                color = Color.Blue,
                strokeWidth = 10f
            )
        }
    }


}