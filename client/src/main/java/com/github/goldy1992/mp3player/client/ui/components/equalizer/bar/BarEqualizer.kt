@file:JvmName("BarEqualizerKt")

package com.github.goldy1992.mp3player.client.ui.components.equalizer.bar

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import com.github.goldy1992.mp3player.client.utils.visualizer.calculateBarSpacingPixels

private const val MAX_AMPLITUDE = 400f
private const val LOG_TAG = "BarEqualizer"
private const val DEFAULT_BAR_HEIGHT = 5f

@Preview
@Composable
fun BarEqualizer(modifier: Modifier = Modifier,
                 frequencyValues : () -> List<Float> = {listOf(100f, 200f, 300f, 150f)},
                 canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
                 barColor : Color = MaterialTheme.colorScheme.secondary,
                 surfaceColor : Color = MaterialTheme.colorScheme.primaryContainer
                       ) {
    Log.v(LOG_TAG, "BarEqualizer() recomposing")
    val frequencyPhases = frequencyValues().ifEmpty { (1..24).map { 0f }.toList() }
    /* recalculate the bar width when the number of frequencyPhases sent changes OR the canvas size changes.
       Fixes a bug when the frequencyPhases array is empty to begin with. */
    val barWidthPx : Float = remember(frequencyPhases.size, canvasSize) {
        if (frequencyPhases.isNotEmpty()) {
            (canvasSize.widthPx / frequencyPhases.size) -5f
        } else {
            Log.w(LOG_TAG, "audio data empty, setting with to 0")
            0f
        }
    }



    val numberOfBars = frequencyPhases.size
    val spaceBetweenBarsPx = remember(numberOfBars, barWidthPx, canvasSize.widthPx){
        calculateBarSpacingPixels(
            containerWidthPx = canvasSize.widthPx,
            numOfBars = numberOfBars,
            barWidthPx = barWidthPx
        )
    }
    val barHorizontalOffsets = remember(spaceBetweenBarsPx, numberOfBars, barWidthPx, canvasSize.widthPx) {
        val list : MutableList<Float> = mutableListOf()
        for (i in 0 until numberOfBars) {
            val offset = (spaceBetweenBarsPx * (i + 1)) + (barWidthPx * i)
            list.add(offset)
        }
        list.toList()
    }

    val barTopLeftVerticalOffsets : MutableList<Float> = mutableListOf()
    val barHeights : MutableList<Float> = mutableListOf()
    val barPoints : MutableList<BarPoints> = mutableListOf()
    for (i in 0 until numberOfBars) {
        val currentAnimatedValue by animateFloatAsState(targetValue = frequencyPhases[i])
        var barHeight = DEFAULT_BAR_HEIGHT + canvasSize.heightPx * (currentAnimatedValue / MAX_AMPLITUDE)
        if (barHeight > canvasSize.heightPx) {
            barHeight = canvasSize.heightPx
        }
        barHeights.add(barHeight)
        val offset = canvasSize.heightPx - barHeight
        barTopLeftVerticalOffsets.add(offset)

        barPoints.add(
            BarPoints(
               Offset( barHorizontalOffsets[i], barTopLeftVerticalOffsets[i]),
                Size(width = barWidthPx, height = barHeight)
            )
        )
    }

    BarEqualizerCanvas(
        bars = barPoints,
        barColor = barColor,
        surfaceColor = surfaceColor,
        modifier = modifier)
}

@Composable
private fun BarEqualizerCanvas(
    bars : List<BarPoints>,
    modifier : Modifier = Modifier,
    surfaceColor: Color = MaterialTheme.colorScheme.primaryContainer,
    barColor: Color = MaterialTheme.colorScheme.secondary,
) {
    Log.i(LOG_TAG, "BarEqualizerCanvas() redraw: ${if (bars.isNotEmpty())  bars[0] else 0f} ")

    Canvas(
        modifier = modifier.fillMaxSize()

    ) {
//        drawRoundRect(color = surfaceColor, size = this.size, cornerRadius = CornerRadius(5f, 5f))

        for (idx in bars.indices) {
            drawRect(
                color = barColor,
                topLeft = bars[idx].offset,
                size = bars[idx].size
            )
        }
    }
}

private data class BarPoints(
    val offset: Offset,
    val size: Size
)
