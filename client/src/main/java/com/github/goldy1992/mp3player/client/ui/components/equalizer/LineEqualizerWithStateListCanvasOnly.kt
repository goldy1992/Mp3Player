package com.github.goldy1992.mp3player.client.ui.components.equalizer

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview

private const val AMPLITUDE = 5f

private const val LOG_TAG = "LineEqualizerWithStateListCanvasOnly"


@Composable
@Preview
fun LineEqualizerWithStateListCanvasOnly(modifier: Modifier = Modifier,
    frequencyPhases : List<Float> = emptyList(),
    insetPx : Float = 200f) {

    val lineColor = MaterialTheme.colorScheme.primary


    val list : SnapshotStateList<Float> = remember(frequencyPhases.size) {
        mutableStateListOf<Float>().apply {
            Log.i(LOG_TAG, "LineEqualizerWithStateListCanvasOnly() re-trigger remember")
            for (i in frequencyPhases) add( 0f) }
    }


    for (i in frequencyPhases.indices) {
        val currentPhase by animateFloatAsState(targetValue = frequencyPhases[i], animationSpec = tween(300))
        list[i] = currentPhase
    }

    Canvas(modifier = modifier
        .fillMaxSize()) {
        val numberOfPhases : Int = frequencyPhases.size
        val phaseSpacing : Float = (size.width - (2 * insetPx ) ) / (numberOfPhases + 1)
        val lineHeight = size.height / 2
        var currentOffset  = Offset(0f, lineHeight)


        var newOffset = Offset(currentOffset.x + insetPx, lineHeight)
        drawLine(
            start = currentOffset,
            end = newOffset,
            color = lineColor
        )
        currentOffset = newOffset
        for (i in list.indices) {
            newOffset = Offset(currentOffset.x + phaseSpacing, lineHeight - (list[i] * AMPLITUDE))
            drawLine(
                start = currentOffset,
                end = newOffset,
                color = lineColor,
            )

            drawLine(
                start = Offset(currentOffset.x, lineHeight + (lineHeight-currentOffset.y)),
                end = Offset(newOffset.x, lineHeight + (list[i] * AMPLITUDE)),
                color = lineColor,
            )
            currentOffset = newOffset
        }

        newOffset = Offset(currentOffset.x + phaseSpacing, lineHeight)
        drawLine(
            start = currentOffset,
            end = newOffset,
            color = lineColor
        )
        drawLine(
            start = Offset(currentOffset.x, lineHeight + (lineHeight-currentOffset.y)),
            end = newOffset,
            color = lineColor,
        )
        currentOffset = newOffset

        drawLine(
            start = currentOffset,
            end = Offset(currentOffset.x + insetPx, lineHeight),
            color = lineColor,
        )

        drawLine(
            start = currentOffset,
            end = Offset(currentOffset.x + insetPx, lineHeight),
            color = lineColor,
        )
    }
}


