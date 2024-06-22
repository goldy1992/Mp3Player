package com.github.goldy1992.mp3player.client.ui.components.equalizer

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

private const val AMPLITUDE = 50f

private const val LOG_TAG = "LineEqualizer"

@Composable
@Preview
fun LineEqualizer(modifier: Modifier = Modifier,
    frequencyPhases : List<Float> = emptyList(),
    insetPx : Float = 200f) {

    val list = remember(frequencyPhases.size) {

        mutableStateListOf<Float>().apply {
            Log.d(LOG_TAG, "LineEqualizer re-trigger list remember")
            for (i in frequencyPhases) add(
        i) } }


    for (i in frequencyPhases.indices) {
        list[i] = animateFloatAsState(targetValue = frequencyPhases[i], animationSpec = tween(300),
            label = "line$i"
        ).value
    }


    BoxWithConstraints {
        val numberOfPhases : Int = frequencyPhases.size
        val maxHeight : MutableState<Float> = remember { mutableFloatStateOf(0f) }
        val maxWidth : MutableState<Float> = remember { mutableFloatStateOf(0f) }
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
            for (i in list.indices) {
                newOffset = Offset(currentOffset.x + phaseSpacing, lineHeight + (list[i] * AMPLITUDE))
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