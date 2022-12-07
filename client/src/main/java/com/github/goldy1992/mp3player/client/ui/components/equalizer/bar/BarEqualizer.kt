@file:JvmName("BarEqualizerKt")

package com.github.goldy1992.mp3player.client.ui.components.equalizer.bar

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import com.github.goldy1992.mp3player.client.utils.calculateBarSpacingPixels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val MAX_AMPLITUDE = 400f
private const val logTag = "BarEqualizer"


@Preview
@Composable
fun BarEqualizer(modifier: Modifier = Modifier,
                 frequencyValues : () -> List<Float> = {listOf(100f, 200f, 300f, 150f)},
                 canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
                 barWidthPx : Float = 20f,
                 barColor : Color = MaterialTheme.colorScheme.secondary,
                 surfaceColor : Color = MaterialTheme.colorScheme.primaryContainer,
                 scope: CoroutineScope = rememberCoroutineScope()
                       ) {

    val frequencyPhases = frequencyValues()
    val frequencyAnimatableList : SnapshotStateList<Animatable<Float, AnimationVector1D>> =
        remember(frequencyPhases.size) {
            mutableStateListOf<Animatable<Float, AnimationVector1D>>().apply {
                Log.i(logTag, "retrigger remember")
                for (i in frequencyPhases) add(Animatable(i))
            }
        }
    LaunchedEffect(frequencyPhases) {
        //    Log.i(logTag, "Triggered launch effect: frequencyPhases $frequencyPhases")
        for (i in frequencyPhases.indices) {
            this.launch { frequencyAnimatableList[i].animateTo(targetValue = frequencyPhases[i], animationSpec = tween(150)) }
        }
    }

    val bars = frequencyAnimatableList.map { it.value }.toList()
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
        var barHeight = canvasSize.heightPx * (bars[i] / MAX_AMPLITUDE)
        if (barHeight > canvasSize.heightPx) {
            barHeight = canvasSize.heightPx
        }
        barHeights.add(barHeight)
        val offset = canvasSize.heightPx - barHeight
        barTopLeftVerticalOffsets.add(offset)
    }

    Canvas(
        modifier = modifier
            .width(canvasSize.widthDp)
            .height(height = canvasSize.heightDp)

    ) {
        drawRoundRect(color = surfaceColor, size = this.size, cornerRadius = CornerRadius(5f, 5f))

        for (idx in bars.indices) {
            drawRect(
                color = barColor,
                topLeft = Offset( barHorizontalOffsets[idx], barTopLeftVerticalOffsets[idx]),
                size = Size(width = barWidthPx, height = (barHeights[idx]))
            )
        }
    }

}
