package com.github.goldy1992.mp3player.client.ui.components.equalizer.pichart

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import com.github.goldy1992.mp3player.client.ui.LocalIsDarkMode
import com.github.goldy1992.mp3player.client.utils.AudioDataUtils.MAX_FREQUENCY_AMPLITUDE

private const val LOG_TAG = "PieChartVisualizer"

@Preview
@Composable
fun PieChartVisualizer(
    modifier: Modifier = Modifier,
    frequencyPhasesState : () -> List<Float> = {listOf(100f, 200f, 300f, 150f)},
    canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
    insetPx : Float = 10f,
    baseColor : Color = MaterialTheme.colorScheme.primaryContainer,
    ) {
    val isDarkMode = LocalIsDarkMode.current
    val colorRange = remember(baseColor, isDarkMode) {
        val rRange = if (isDarkMode) 1 - baseColor.red else baseColor.red
    val gRange =  if (isDarkMode) 1 - baseColor.green else baseColor.green
    val bRange =  if (isDarkMode) 1 - baseColor.blue else baseColor.blue
        ColorRange (rRange, gRange, bRange)
    }
    val frequenciesValue = frequencyPhasesState()
    val pieSegments = mutableListOf<PieSegment>()
    val frequencies = frequenciesValue.ifEmpty { (1..24).map { 0f }.toList() }
    val maxRadius = remember(canvasSize) {
        (maxOf(canvasSize.widthPx, canvasSize.heightPx) / 2f) - (2 * insetPx)
    }
    val defaultRadius = remember(maxRadius) {
        maxRadius * 0.8f
    }
    val frequencyRange = remember(maxRadius, defaultRadius) {
        maxRadius - defaultRadius
    }
    val defaultOffset = remember(canvasSize) {
        Offset((canvasSize.widthPx / 2), (canvasSize.heightPx / 2))
    }
    val sweepAngle = remember(frequencies.size) {
        360f / (if (frequencies.isNotEmpty()) {
            frequencies.size
        } else 15)
    }
    var currentAngle = 0f


    frequencies.forEachIndexed { index, f ->
        val currentAnimatedValue by animateFloatAsState(
            targetValue = f,
            label = "pieChartFrequency[$index]"
        )

        val division = currentAnimatedValue / MAX_FREQUENCY_AMPLITUDE
        val fractionOfAmplification = if (division > 1.0f) 1.0f else if (division < 0f) 0f else division
        val targetColor = calculateTargetColor(baseColor, colorRange, fractionOfAmplification, isDarkMode)
        val animatedColor by animateColorAsState(
            targetValue = targetColor,
            label = "pieChartColor[$index]"
        )

        val adjustment = frequencyRange * fractionOfAmplification
        val width = defaultRadius + adjustment
        val height = defaultRadius + adjustment
        val size = Size(width, height)
        val arcOffset =
            Offset(defaultOffset.x - (width / 2), defaultOffset.y - (height / 2))
        pieSegments.add(
            PieSegment(
                color = animatedColor,
                arcOffset = arcOffset,
                size = size
            )
        )

    }

    Canvas(modifier.fillMaxSize()) {
        for (segment in pieSegments) {
            drawArc(
                color = segment.color,
                startAngle = currentAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = segment.arcOffset,
                size = segment.size
            )

            currentAngle += sweepAngle
        }
    }
}
private fun calculateTargetColor(
    baseColor : Color,
    colorRange: ColorRange,
    fractionOfAmplification : Float,
    isDarkMode : Boolean) : Color {
    val targetR : Float
    val targetG : Float
    val targetB : Float
    if (isDarkMode) {
        targetR = baseColor.red + (colorRange.red * fractionOfAmplification)
        targetG = baseColor.green + (colorRange.green * fractionOfAmplification)
        targetB = baseColor.blue + (colorRange.blue * fractionOfAmplification)
    } else {
        targetR = baseColor.red - (colorRange.red * fractionOfAmplification)
        targetG = baseColor.green - (colorRange.green * fractionOfAmplification)
        targetB = baseColor.blue - (colorRange.blue * fractionOfAmplification)
    }
    Log.v(
        LOG_TAG,
        "rRange: ${colorRange.red}, gRange: ${colorRange.green}, bRange: ${colorRange.blue}, fractionOfAmplification: $fractionOfAmplification"
    )
    return Color(red = targetR, green = targetG, blue = targetB)

}

private data class PieSegment (
    val color : Color,
    val arcOffset : Offset,
    val size : Size
)

private data class ColorRange (
    val red : Float,
    val green : Float,
    val blue : Float
)

