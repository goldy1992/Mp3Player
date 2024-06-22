package com.github.goldy1992.mp3player.client.ui.components.equalizer.pichart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import com.github.goldy1992.mp3player.client.utils.AudioDataUtils
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@Composable
fun rememberPieChartVisualizerState(
    scope : CoroutineScope = rememberCoroutineScope(),
    insetPx: Float,
    isDarkMode: Boolean,
    baseColor: Color
): PieChartVisualizerState = remember(scope, insetPx, isDarkMode, baseColor) {

    PieChartVisualizerState(scope, insetPx, isDarkMode, baseColor)
}


@Stable
class PieChartVisualizerState(
    val scope: CoroutineScope,
    val insetPx : Float,
    private val isDarkMode : Boolean,
    private val baseColor: Color
) : LogTagger {

    var canvasSize: IntSize = IntSize.Zero
    private var maxRadius = 10f
    private var defaultRadius = maxRadius * 0.8f
    private var frequencyRange = maxRadius - defaultRadius
    private var defaultOffset = Offset((canvasSize.width / 2f), (canvasSize.height / 2f))
    private val colorRange = createColorRange(baseColor)


    private val _pieSegments = MutableStateFlow<List<PieSegment>>(emptyList())
    val pieSegmentsState: StateFlow<List<PieSegment>> = _pieSegments

    fun setSize(size: IntSize) {
        this.canvasSize = size
        maxRadius = (minOf(canvasSize.width, canvasSize.height) / 2f) - (2 * insetPx)
        defaultRadius = maxRadius * 1.5f
        frequencyRange = maxRadius - defaultRadius
        defaultOffset = Offset((canvasSize.width / 2f), (canvasSize.height / 2f))
    }

    override fun logTag(): String {
        return "PieChartVisualizerState"
    }

    fun setFrequencies(animatedFrequencies: List<Float>) {
        scope.launch { calculateNewPieSegments(animatedFrequencies) }
    }

    private fun calculateNewPieSegments(frequencies: List<Float>) {

        val pieSegments = mutableListOf<PieSegment>()

        frequencies.forEach { currentAnimatedValue ->
            val division = currentAnimatedValue / AudioDataUtils.MAX_FREQUENCY_AMPLITUDE
            val fractionOfAmplification =
                if (division > 1.0f) 1.0f else if (division < 0f) 0f else division
            val targetColor =
                calculateTargetColor(baseColor, colorRange, fractionOfAmplification, isDarkMode)

            val adjustment = frequencyRange * fractionOfAmplification
            val width = defaultRadius + adjustment
            val height = defaultRadius + adjustment
            val size = Size(width, height)
            val arcOffset =
                Offset(defaultOffset.x - (width / 2), defaultOffset.y - (height / 2))
            pieSegments.add(
                PieSegment(
                    color = targetColor,
                    arcOffset = arcOffset,
                    size = size
                )
            )
            this._pieSegments.value = pieSegments
        }
    }

    private fun calculateTargetColor(
        baseColor: Color,
        colorRange: ColorRange,
        fractionOfAmplification: Float,
        isDarkMode: Boolean
    ): Color {
        val targetR: Float
        val targetG: Float
        val targetB: Float
        if (isDarkMode) {
            targetR = baseColor.red + (colorRange.red * fractionOfAmplification)
            targetG = baseColor.green + (colorRange.green * fractionOfAmplification)
            targetB = baseColor.blue + (colorRange.blue * fractionOfAmplification)
        } else {
            targetR = baseColor.red - (colorRange.red * fractionOfAmplification)
            targetG = baseColor.green - (colorRange.green * fractionOfAmplification)
            targetB = baseColor.blue - (colorRange.blue * fractionOfAmplification)
        }
        return Color(red = targetR, green = targetG, blue = targetB)

    }

    private fun createColorRange(baseColor: Color): ColorRange {
        val rRange = if (isDarkMode) 1 - baseColor.red else baseColor.red
        val gRange = if (isDarkMode) 1 - baseColor.green else baseColor.green
        val bRange = if (isDarkMode) 1 - baseColor.blue else baseColor.blue
        return ColorRange(rRange, gRange, bRange)

    }
}

