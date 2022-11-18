package com.github.goldy1992.mp3player.client.ui.components.equalizer

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.screens.DpPxSize
import com.github.goldy1992.mp3player.client.utils.calculateBarSpacingPixels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val MAX_AMPLITUDE = 400f
private const val logTag = "BarEqualizer"


@Composable
fun BarEqualizerCardImpl(
    modifier: Modifier = Modifier,
    barsState : () -> List<Float> = {listOf(100f, 200f, 300f, 150f)},
    barColor : Color = MaterialTheme.colorScheme.secondary,
    canvasDpPxSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
    barWidthPx : Float = 20f,
    waveScaleFactor : Float = 5f,
    scope : CoroutineScope = rememberCoroutineScope()
) {
    val bars = barsState()
    val numberOfBars = bars.size
    val barStates : SnapshotStateList<Animatable<Float, AnimationVector1D>> = remember(bars.size) {
        mutableStateListOf<Animatable<Float, AnimationVector1D>>().apply {
            Log.i(logTag, "retrigger remember")
            for (i in bars) add( Animatable(i)) }
    }

    val spaceBetweenBarsPx = remember(numberOfBars, barWidthPx, canvasDpPxSize.widthPx){
        calculateBarSpacingPixels(
            containerWidthPx = canvasDpPxSize.widthPx,
            numOfBars = bars.size,
            barWidthPx = barWidthPx
        )
    }

    val barHorizontalOffsets = remember(spaceBetweenBarsPx, numberOfBars, barWidthPx, canvasDpPxSize.widthPx) {
        val list : MutableList<Float> = mutableListOf()
        for (i in bars.indices) {
            val offset = (spaceBetweenBarsPx * (i + 1)) + (barWidthPx * i)
            list.add(offset)
        }
        list.toList()
    }

    val barTopLeftVerticalOffsets : MutableList<Float> = mutableListOf()
    val barHeights : MutableList<Float> = mutableListOf()
    for (i in bars.indices) {
        var barHeight = canvasDpPxSize.heightPx * (1 - (barStates[i].value / MAX_AMPLITUDE))
        if(barHeight > canvasDpPxSize.heightPx) {
            barHeight = canvasDpPxSize.heightPx
        }
        barHeights.add(barHeight)
        val offset = canvasDpPxSize.heightPx - barHeight
        barTopLeftVerticalOffsets.add(offset)
    }

    LaunchedEffect(bars) {
        Log.i(logTag, "Triggered launch effect")
        for (i in bars.indices) {
            scope.launch { barStates[i].animateTo(targetValue = bars[i], animationSpec = tween(300)) }
        }
    }
    val surfaceColor = MaterialTheme.colorScheme.primaryContainer

    Canvas(
        modifier = modifier.fillMaxSize()

    ) {
        drawRoundRect(color = surfaceColor, size = this.size, cornerRadius = CornerRadius(5f, 5f))

        for (idx in barStates.indices) {
            drawRect(
                color = barColor,
                topLeft = Offset( barHorizontalOffsets[idx], barTopLeftVerticalOffsets[idx]),
                size = Size(width = barWidthPx, height = (barHeights[idx]))
            )
        }
    }

}





@Preview
@Composable
fun BarEqualizerCanvas(  modifier: Modifier = Modifier,
                         frequencyValues : () -> List<Float> = {listOf(100f, 200f, 300f, 150f)},
                        canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
                        barWidthPx : Float = 20f,
                        barColor : Color = MaterialTheme.colorScheme.secondary,
                        surfaceColor : Color = MaterialTheme.colorScheme.primaryContainer,
                       ) {

    val bars = frequencyValues()
    val numberOfBars = bars.size
    val spaceBetweenBarsPx = remember(numberOfBars, barWidthPx, canvasSize.widthPx){
        calculateBarSpacingPixels(
            containerWidthPx = canvasSize.widthPx,
            numOfBars = bars.size,
            barWidthPx = barWidthPx
        )
    }

    val barHorizontalOffsets = remember(spaceBetweenBarsPx, numberOfBars, barWidthPx, canvasSize.widthPx) {
        val list : MutableList<Float> = mutableListOf()
        for (i in bars.indices) {
            val offset = (spaceBetweenBarsPx * (i + 1)) + (barWidthPx * i)
            list.add(offset)
        }
        list.toList()
    }

    val barTopLeftVerticalOffsets : MutableList<Float> = mutableListOf()
    val barHeights : MutableList<Float> = mutableListOf()
    for (i in bars.indices) {
        var barHeight = canvasSize.heightPx * (1 - (bars[i] / MAX_AMPLITUDE))
        if(barHeight > canvasSize.heightPx) {
            barHeight = canvasSize.heightPx
        }
        barHeights.add(barHeight)
        val offset = canvasSize.heightPx - barHeight
        barTopLeftVerticalOffsets.add(offset)
    }

    Canvas(
        modifier = modifier.width(canvasSize.widthDp)
            .height(height = canvasSize.heightDp)

    ) {
        drawRoundRect(color = surfaceColor, size = this.size, cornerRadius = CornerRadius(5f, 5f))
        drawLine(Color.Green, start = Offset(0f, 5f), end = Offset(x = spaceBetweenBarsPx, y = 5f))

        drawLine(Color.Green, start = Offset(0f, 10f), end = Offset(x = canvasSize.widthPx, y = 10f))
        for (idx in bars.indices) {
            drawRect(
                color = barColor,
                topLeft = Offset( barHorizontalOffsets[idx], barTopLeftVerticalOffsets[idx]),
                size = Size(width = barWidthPx, height = (barHeights[idx]))
            )
        }
    }

}
