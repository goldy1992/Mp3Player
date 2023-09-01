package com.github.goldy1992.mp3player.client.ui.components.equalizer.pichart

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import com.github.goldy1992.mp3player.client.ui.LocalIsDarkMode
import com.github.goldy1992.mp3player.client.utils.AudioDataUtils.MAX_FREQUENCY_AMPLITUDE
import kotlinx.coroutines.CoroutineScope

private const val LOG_TAG = "PieChartVisualizer"

@Preview
@Composable
fun PieChartVisualizer(
    modifier: Modifier = Modifier,
    frequencyPhasesState : () -> List<Float> = {listOf(100f, 200f, 300f, 150f)},
    insetPx : Float = 10f,
    baseColor : Color = MaterialTheme.colorScheme.primaryContainer,
    scope: CoroutineScope = rememberCoroutineScope(),
    pieChartVisualizerState: PieChartVisualizerState = rememberPieChartVisualizerState(scope = scope, insetPx = insetPx, LocalIsDarkMode.current, baseColor)
    ) {
    val frequenciesValue = frequencyPhasesState()
    val frequencies = frequenciesValue.ifEmpty { (1..24).map { 0f }.toList() }
    val animatedFrequencies = mutableListOf<Float>()
    frequencies.forEachIndexed { index, f ->
        val currentAnimatedValue by animateFloatAsState(
            targetValue = f,
            label = "pieChartFrequency[$index]"
        )
        animatedFrequencies.add(currentAnimatedValue)
    }
    pieChartVisualizerState.setFrequencies(animatedFrequencies)

    val sweepAngle = remember(frequencies.size) {
        360f / (if (frequencies.isNotEmpty()) {
            frequencies.size
        } else 15)
    }
    var currentAngle = 0f

    val pieSegments by pieChartVisualizerState.pieSegmentsState.collectAsState()
    Canvas(
        modifier
            .fillMaxSize()
            .onSizeChanged { pieChartVisualizerState.setSize(it) }) {
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



