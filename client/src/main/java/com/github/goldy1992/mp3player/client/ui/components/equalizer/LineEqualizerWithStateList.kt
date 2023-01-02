package com.github.goldy1992.mp3player.client.ui.components.equalizer

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

private const val AMPLITUDE = 50f

private const val logTag = "LineEqualizer"


private var fp : List<Float> = emptyList()

@Composable
@Preview
fun LineEqualizerWithStateList(modifier: Modifier = Modifier,
    frequencyPhases : List<Float> = emptyList(),
    insetPx : Float = 200f) {

    if (frequencyPhases == fp) {
        Log.i(logTag, "recomposition")
    } else {
        fp = frequencyPhases
        Log.i(logTag, "new data")
    }

    val list : SnapshotStateList<Float> = remember(frequencyPhases.size) {
        mutableStateListOf<Float>().apply {
            Log.i(logTag, "retrigger remember")
            for (i in frequencyPhases) add( 0f) }
    }


    for (i in frequencyPhases.indices) {
        val currentPhase by animateFloatAsState(targetValue = frequencyPhases[i], animationSpec = tween(300))
        list[i] = currentPhase
    }


    BoxWithConstraints() {
        val numberOfPhases : Int = frequencyPhases.size
        val maxHeight : MutableState<Float> = remember { mutableStateOf(0f) }
        val maxWidth : MutableState<Float> = remember { mutableStateOf(0f) }
        val phaseSpacing : Float = (maxWidth.value - (2 * insetPx ) ) / (numberOfPhases + 1)
        val lineHeight = this.maxHeight.value / 2
        var currentOffset  = Offset(0f, lineHeight)

        Canvas(modifier = modifier
            .fillMaxSize()
            .background(Color.Red)) {
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