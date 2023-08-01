package com.github.goldy1992.mp3player.client.ui.components.equalizer.pichart

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
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
import com.github.goldy1992.mp3player.client.utils.AudioDataUtils.MAX_FREQUENCY_AMPLITUDE

@Preview
@Composable
fun PieChartVisualizer(
    modifier: Modifier = Modifier,
    frequencyPhasesState : () -> List<Float> = {listOf(100f, 200f, 300f, 150f)},
    canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
    insetPx : Float = 10f,
    firstColor : Color = MaterialTheme.colorScheme.onPrimaryContainer,
    alternateColor : Color = MaterialTheme.colorScheme.primary,


    ) {
    val rRange = remember(firstColor) { 1 - firstColor.red}
    val gRange = remember(firstColor) { 1 - firstColor.green}
    val bRange = remember(firstColor) { 1 - firstColor.blue}
    val frequenciesValue = frequencyPhasesState()
    val frequencies = frequenciesValue.ifEmpty { (1..24).map { 0f }.toList() }
    val maxRadius = (maxOf(canvasSize.widthPx, canvasSize.heightPx) / 2f) - (2 * insetPx)
    val defaultRadius = maxRadius * 0.5f
    val frequencyRange = maxRadius - defaultRadius
    val defaultOffset = Offset((canvasSize.widthPx /2), (canvasSize.heightPx /2))
    val sweepAngle = 360f / (if (frequencies.isNotEmpty()) {frequencies.size } else 15)
    var currentAngle = 0f




    val animatedFrequencies = mutableListOf<Pair<Float, Color>>()
    frequencies.forEachIndexed { index, f ->
        val currentAnimatedValue by animateFloatAsState(targetValue = f, label = "pieChartFrequency[$index]")



        val targetR = (MaterialTheme.colorScheme.primary.red + (rRange * (f / MAX_FREQUENCY_AMPLITUDE)))
        val targetG = (MaterialTheme.colorScheme.primary.green + (gRange * (f / MAX_FREQUENCY_AMPLITUDE)))
        val targetB = (MaterialTheme.colorScheme.primary.blue + (bRange * (f / MAX_FREQUENCY_AMPLITUDE)))
        val targetColor = Color(red = targetR, green = targetG, blue = targetB)
        val animatedColor by animateColorAsState(targetValue = targetColor, label = "pieChartColor[$index]")
        animatedFrequencies.add(Pair(currentAnimatedValue, animatedColor))
    }

    var useAlternateColor = false
    Canvas(modifier.fillMaxSize() ) {
        for (f in animatedFrequencies) {
                val color = f.second
                val adjustment = frequencyRange * (f.first / MAX_FREQUENCY_AMPLITUDE)
                val width = defaultRadius + adjustment
                val height = defaultRadius + adjustment
                val size = Size(width, height)
                val arcOffset =
                    Offset(defaultOffset.x - (width / 2), defaultOffset.y - (height / 2))

                useAlternateColor = !useAlternateColor
                drawArc(
                    color,
                    startAngle = currentAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = arcOffset,
                    size = size
                )

                currentAngle += sweepAngle
        }


    }
}