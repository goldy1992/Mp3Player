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
                     canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(300.dp, 1000.dp, LocalDensity.current),
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
    val minRadius = (0.9f * minOf(canvasSize.widthPx, canvasSize.heightPx))  / 2.1f
    val frequencyCoordinates = offsets(animatedFrequencies, minRadius)
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


         minRadius
        drawPath(
            curvePath,
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent,
                    lineColor,
                ),
                center = center,
                radius = if (minRadius > 0) minRadius else 200f
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

        for (c in coordinates) {
            drawCircle(Color.Red, radius = 5f, center = c)
        }

    }
}



@Preview(widthDp =  500, heightDp = 1000)
@Composable
fun CircularEqualizerNewImpl(modifier: Modifier = Modifier,
                      frequencyPhasesState : () -> List<Float> = {listOf(100f, 200f, 100f, 250f)},
                      canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(500.dp, 1000.dp, LocalDensity.current),
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


    val minRadius = (0.9f * minOf(canvasSize.widthPx, canvasSize.heightPx))  / 2.1f
    val frequencyCoordinates = offsetCoordinates(animatedFrequencies, minRadius, center)

    val lines = mutableListOf<CubicBezierCurve>()
    for (i in 0 until frequencyCoordinates.size - 1) {
        val startPoint = frequencyCoordinates[i]
        val endPoint = frequencyCoordinates[i+1]
        val controlPoint1 : CircleCoordinate
        val controlPoint2 : CircleCoordinate

        val angleDiff = endPoint.angle - startPoint.angle
        val controlPoint1Angle = startPoint.angle + (angleDiff / 3f)
        val controlPoint2Angle = startPoint.angle + ((2 * angleDiff) / 3f)
        val radiusToUse = (startPoint.radius + endPoint.radius) / 2f

        controlPoint1 = CircleCoordinate(
                offset = Offset(
                    x = center.x + (radiusToUse * cos(controlPoint1Angle)),
                    y = center.y + (radiusToUse * sin(controlPoint1Angle))),
                radius = radiusToUse,
                angle = controlPoint1Angle
        )
        controlPoint2 = CircleCoordinate(
            offset = Offset(
                x = center.x + (radiusToUse * cos(controlPoint2Angle)),
                y = center.y + (radiusToUse * sin(controlPoint2Angle))),
            radius = radiusToUse,
            angle = controlPoint2Angle
        )


        lines.add(CubicBezierCurve(
            from = startPoint,
            to = endPoint,
            controlPoint1 = controlPoint1,
            controlPoint2 = controlPoint2
        ))
    }
    Log.i(LOG_TAG, "lines : $lines")


    Canvas(modifier = modifier
        .fillMaxSize()

    ) {
        val curvePath = Path().apply {
            reset()
            if (frequencyCoordinates.isNotEmpty()) {
                val firstVal = frequencyCoordinates.first().offset
                moveTo(firstVal.x, firstVal.y)
                for (i in lines)
                    cubicTo(
                        i.controlPoint1.offset.x,  i.controlPoint1.offset.y,
                        i.controlPoint2.offset.x,  i.controlPoint2.offset.y,
                        i.to.offset.x, i.to.offset.y
                    )
                close()
            }

        }



//        drawPath(
//            curvePath,
//            brush = Brush.radialGradient(
//                colors = listOf(Color.Transparent,
//                    lineColor,
//                ),
//                center = center,
//                radius = if (minRadius > 0) minRadius else 200f
//            ),
//        )
        drawPath(
            path = curvePath,
            color = lineColor,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )

        for (c in frequencyCoordinates) {
            drawCircle(Color.Red, radius = 5f, center = c.offset)
        }

        lines.forEachIndexed { idx, l ->

                drawCircle(Color.Green, radius = 5f, center = l.controlPoint1.offset)
                drawCircle(Color.Yellow, radius = 5f, center = l.controlPoint2.offset)

        }

    }
}

private fun offsets(frequencies: List<Float>, minRadius : Float) : List<Offset> {

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

private fun offsetCoordinates(frequencies: List<Float>, minRadius : Float, center : Offset) : List<CircleCoordinate> {

    val maxFreqRadius = 150f
    val maxFreq = 300
    val toReturn = mutableListOf<CircleCoordinate>()
    var angle = 0f
    if (frequencies.isNotEmpty()) {
        val spacing = ((2 * Math.PI) / frequencies.count()).toFloat()

        for (frequency in frequencies) {
            val radius = minRadius + (maxFreqRadius * (frequency / maxFreq))
            val x = (radius * cos(angle)) + center.x
            val y = (radius * sin(angle)) + center.y
            val offset = Offset(x, y)
            toReturn.add(CircleCoordinate(offset, radius, angle))
            Log.i(LOG_TAG, "Offset: ${offset}")
            angle += spacing
        }
        toReturn.add(CircleCoordinate(toReturn[0].offset, toReturn[0].radius, Math.PI.toFloat() * 2))
    } else {
        val spacing = ((2 * Math.PI) / 15).toFloat()
        for (i in 1..15) {
            val x = (minRadius * cos(angle)) + center.x
            val y = (minRadius * sin(angle)) + center.y
            toReturn.add(CircleCoordinate(Offset(x, y), minRadius, angle))
            angle += spacing
        }
        toReturn.add(toReturn[0])

    }


    return toReturn.toList()
}


@Preview
@Composable
private fun CircleGradient() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawPath(path = Path().apply {
            moveTo(size.width / 2, 0f)
            lineTo(size.width, size.height / 2)
            lineTo(size.width / 2, size.height)
            lineTo(0f, size.height / 2)
            close()
        },
            brush = Brush.verticalGradient(listOf(
            Color.Yellow, Color.Transparent))
        )
    }
}

private data class CircleCoordinate(
    val offset: Offset,
    val radius : Float,
    /** Radians */
    val angle : Float
)
private data class CubicBezierCurve(
    val from : CircleCoordinate,
    val to : CircleCoordinate,
    val controlPoint1 : CircleCoordinate,
    val controlPoint2: CircleCoordinate
)