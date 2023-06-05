package com.github.goldy1992.mp3player.client.ui.components.equalizer.smoothline

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.DpPxSize

private const val logTag = "SmoothEqualizer"

@Composable
fun SmoothLineEqualizer(modifier: Modifier = Modifier,
                        frequencyPhasesState : () -> List<Float> = {listOf(100f, 200f, 300f, 150f)},
                        canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
                        insetPx : Float = 10f,
                        surfaceColor : Color = MaterialTheme.colorScheme.primaryContainer,
                        lineColor : Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    val frequencyPhases = frequencyPhasesState()
    val numberOfPhases: Int = frequencyPhases.size
    val phaseSpacing = remember(
        canvasSize,
        numberOfPhases
    ) { (canvasSize.widthPx - (2 * insetPx)) / (numberOfPhases + 1) }
    val lineHeight = remember(canvasSize) { (canvasSize.heightPx * 0.9f)}
    val startOffset = remember(lineHeight) { Offset(0f, lineHeight) }
    val waveStartOffset =
        remember(startOffset, insetPx) { Offset(startOffset.x + insetPx, lineHeight) }
    val waveEndOffset = remember(
        numberOfPhases,
        waveStartOffset
    ) {
        Offset(
            waveStartOffset.x + (phaseSpacing * (numberOfPhases + 1)),
            y = lineHeight
        )
    }
    val endOffset = remember(
        numberOfPhases,
        waveStartOffset,
        canvasSize
    ) { Offset(canvasSize.widthPx, y = lineHeight) }
    val coordinates: MutableList<Offset> = mutableListOf()
    coordinates.add(0, startOffset)
    coordinates.add(1, waveStartOffset)

    val frequencyCoordinates = mutableListOf<Offset>()
    for (i in frequencyPhases.indices) {
        val currentANimatedValue by animateFloatAsState(targetValue = frequencyPhases[i])
        frequencyCoordinates.add(Offset(
            x = waveStartOffset.x + (phaseSpacing * (i + 1)),
            y = lineHeight - currentANimatedValue
        ))
    }

    coordinates.addAll(frequencyCoordinates)
    coordinates.add(waveEndOffset)
    coordinates.add(endOffset)

    val controlPoints1: List<Offset> =
        (1 until coordinates.size)
            .map {
                Offset(
                    x = (coordinates[it].x + coordinates[it - 1].x) / 2,
                    y = coordinates[it - 1].y
                )
            }
            .toList()

    val controlPoints2: List<Offset> =
        (1 until coordinates.size)
            .map {
                Offset(
                    x = (coordinates[it].x + coordinates[it - 1].x) / 2,
                    y = coordinates[it].y
                )
            }
            .toList()

    Canvas(modifier = modifier
        .fillMaxSize()

    ) {
        drawRoundRect(color = surfaceColor, size = this.size, cornerRadius = CornerRadius(5f, 5f))
        val curvePath = Path().apply {
            reset()
            if (coordinates.isNotEmpty()) {
                moveTo(coordinates.first().x, coordinates.first().y)
                for (i in 0 until coordinates.size - 1) {
                    cubicTo(
                        controlPoints1[i].x, controlPoints1[i].y,
                        controlPoints2[i].x, controlPoints2[i].y,
                        coordinates[i + 1].x, coordinates[i + 1].y
                    )
                }
            }
        }

        /** filling the area under the path */
        /** filling the area under the path */
        val fillPath = android.graphics.Path(curvePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(coordinates.last().x, lineHeight)
                lineTo(0f, lineHeight)
                close()
            }
        drawPath(
            fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    lineColor,
                    Color.Transparent,
                ),
                endY = lineHeight
            ),
        )
        drawPath(
            path = curvePath,
            color = lineColor,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )
    }
}