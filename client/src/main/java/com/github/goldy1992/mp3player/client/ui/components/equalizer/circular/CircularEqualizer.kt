package com.github.goldy1992.mp3player.client.ui.components.equalizer.circular

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import kotlin.math.cos
import kotlin.math.sin

private const val LOG_TAG = "CircularEqualizer"

@Preview
@Composable
fun CircularEqualizer(modifier: Modifier = Modifier,
                     frequencyPhasesState : () -> List<Float> = {listOf(100f, 200f, 300f, 150f)},
                     canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
                     insetPx : Float = 10f,
                     surfaceColor : Color = MaterialTheme.colorScheme.primaryContainer,
                     lineColor : Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {

    val frequencyPhases = frequencyPhasesState()
    val center = Offset(canvasSize.widthPx / 2, canvasSize.heightPx / 2)
    var animatedFrequencies = mutableListOf<Float>()
    frequencyPhases.forEachIndexed {
        idx, frequency ->
        val animatedFrequency by animateFloatAsState(targetValue = frequency, label = "frequency[$idx]")
        animatedFrequencies.add(animatedFrequency)
    }


    val coordinates: MutableList<Offset> = mutableListOf()

    val frequencyCoordinates = offsets(animatedFrequencies, maxOf(canvasSize.widthPx, canvasSize.heightPx))
        .map { Offset(it.x + center.x, it.y + center.y) }.toList()

    coordinates.addAll(frequencyCoordinates)


    val controlPoints1: List<Offset> =
        (1 until coordinates.size)
            .map {
                Offset(
                    x = (coordinates[it].x + coordinates[it - 1].x) / 2,
                    y = coordinates[it - 1].y
                )
            }    //    .map { Offset(it.x + center.x, it.y + center.y) }
            .toList()

    val controlPoints2: List<Offset> =
        (1 until coordinates.size)
            .map {
                Offset(
                    x = (coordinates[it].x + coordinates[it - 1].x) / 2,
                    y = coordinates[it].y
                )
            }
          //  .map { Offset(it.x + center.x, it.y + center.y) }
      .toList()

    Canvas(modifier = modifier
        .fillMaxSize()

    ) {
        //    drawRoundRect(color = surfaceColor, size = this.size, cornerRadius = CornerRadius(5f, 5f))

        val curvePath = Path().apply {
            reset()
            if (coordinates.isNotEmpty()) {
                moveTo(coordinates.first().x, coordinates.first().y)
                for (i in 0 until coordinates.size - 1) {
//                    lineTo( coordinates[i + 1].x, coordinates[i + 1].y)
//                                        quadraticBezierTo(
//                        controlPoints1[i].x, controlPoints1[i].y,
//                      //  controlPoints2[i].x, controlPoints2[i].y,
//                        coordinates[i + 1].x, coordinates[i + 1].y
//                    )
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
        /** filling the area under the path */
        /** filling the area under the path */
        val lineHeight = 50f
        val fillPath = Path()
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

private fun offsets(frequencies: List<Float>, maxCanvasInstance : Float) : List<Offset> {
    val minRadius = (maxCanvasInstance / 2.1f) - 150f
    val maxFreqRadius = 150f
    val maxFreq = 300
    val toReturn = mutableListOf<Offset>()
    var angle = 0f
    if (frequencies.isNotEmpty()) {
        val spacing = ((2 * Math.PI) / frequencies.count()).toFloat()


        for (frequency in frequencies) {
            val radius = minRadius + (maxFreqRadius * (frequency / maxFreq))
            val x = radius * cos(angle)
            val y = radius * sin(angle)
            val offset = Offset(x, y)
            toReturn.add(offset)
            Log.i(LOG_TAG, "Offset: ${offset}")
            angle += spacing
        }
        toReturn.add(toReturn[0])
    } else {
        val spacing = ((2 * Math.PI) / 15).toFloat()
        for (i in 1..15) {
            val x = minRadius * cos(angle)
            val y = minRadius * sin(angle)
            toReturn.add(Offset(x, y))
            angle += spacing
        }
        toReturn.add(toReturn[0])

    }


    return toReturn.toList()
}