package com.github.goldy1992.mp3player.client.ui.components.equalizer.circular

import android.content.res.Configuration
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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

private const val LOG_TAG = "CircleVisualizerNew"
@OptIn(ExperimentalTextApi::class)
@Preview(widthDp =  500, heightDp = 1000, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CircularEqualizerNewImpl(modifier: Modifier = Modifier,
                             frequencyPhasesState : () -> List<Float> = {listOf(0f, 0f, 0f, 0f, 0f, 300f, 0f)},
                             canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(500.dp, 1000.dp, LocalDensity.current),
                             insetPx : Float = 10f,
                             surfaceColor : Color = MaterialTheme.colorScheme.primaryContainer,
                             lineColor : Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    val textMeasurer = rememberTextMeasurer()

    val frequencyPhases = frequencyPhasesState()
    val center = Offset(canvasSize.widthPx / 2, canvasSize.heightPx / 2)
    var animatedFrequencies = mutableListOf<Float>()
    frequencyPhases.forEachIndexed {
            idx, frequency ->
        val animatedFrequency by animateFloatAsState(targetValue = frequency, label = "frequency[$idx]")
        animatedFrequencies.add(animatedFrequency)
    }


    val minRadius = (0.9f * minOf(canvasSize.widthPx - (2 * insetPx), canvasSize.heightPx - (2 * insetPx)))  / 2.1f
    val frequencyCoordinates = offsetCoordinates(animatedFrequencies, minRadius, center)

    val lines = mutableListOf<CubicBezierCurve>()
    for (i in 0 until frequencyCoordinates.size - 1) {
        val startPoint = frequencyCoordinates[i]
        val endPoint = frequencyCoordinates[i+1]
        lines.add(createCubicBezierCurve(startPoint, endPoint, center))
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
    //    drawCircle(Color.Green, radius = minRadius, center = center)
        drawPath(
            path = curvePath,
            color = lineColor,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )



        frequencyCoordinates.forEachIndexed { index, c ->
         //   drawCircle(Color.Red, radius = 10f, center = c.offset)
    //        drawText(textMeasurer, "${c.angle}", topLeft = c.offset)
        }

        lines.forEachIndexed { idx, l ->
         //   if (idx == 0) {
       //         drawCircle(Color.Blue, radius = 10f, center = l.controlPoint1.offset)
//                drawText(textMeasurer, "${l.controlPoint1.angle}", topLeft = l.controlPoint1.offset)
        //        drawCircle(Color.Magenta, radius = 10f, center = l.controlPoint2.offset)
//                drawText(textMeasurer, "${l.controlPoint2.angle}", topLeft = l.controlPoint2.offset)
     //       }
        }

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
        toReturn.add(CircleCoordinate(toReturn[0].offset, toReturn[0].radius, Math.PI.toFloat() * 2))


    }


    return toReturn.toList()
}


private fun createCubicBezierCurve(
    from : CircleCoordinate,
    to : CircleCoordinate,
    center : Offset
) : CubicBezierCurve {
    val controlPoint1 : CircleCoordinate
    val controlPoint2 : CircleCoordinate

    val angleDiff = to.angle - from.angle
    val controlPoint1Angle = from.angle + (angleDiff / 3f)
    val controlPoint2Angle = from.angle + ((2 * angleDiff) / 3f)
    val controlPoint1Radius : Float
    val controlPoint2Radius : Float
    val radialDiff = abs(from.radius-to.radius)
    //val radiusToUse = ((from.radius + to.radius) / 2f) * 1.01f
    val radiusToUse : Float
    if (abs(from.radius - to.radius) < 15) {
        controlPoint1Radius = maxOf(from.radius, to.radius) * 1.01f
        controlPoint2Radius = controlPoint1Radius
    }
    else if (to.radius > from.radius) {
        radiusToUse = minOf(from.radius, to.radius) * 0.95f
        controlPoint1Radius = minOf(from.radius, to.radius) * 0.95f
        controlPoint2Radius = maxOf(from.radius, to.radius) * 1.05f
    }
    else {
        radiusToUse = maxOf(from.radius, to.radius) * 1.05f
        controlPoint1Radius = maxOf(from.radius, to.radius) * 1.05f
        controlPoint2Radius = minOf(from.radius, to.radius) * 0.95f

    }
    controlPoint1 = CircleCoordinate(
        offset = Offset(
            x = center.x + (controlPoint1Radius * cos(controlPoint1Angle)),
            y = center.y + (controlPoint1Radius * sin(controlPoint1Angle))),
        radius = controlPoint1Radius,
        angle = controlPoint1Angle
    )
    controlPoint2 = CircleCoordinate(
        offset = Offset(
            x = center.x + (controlPoint2Radius * cos(controlPoint2Angle)),
            y = center.y + (controlPoint2Radius * sin(controlPoint2Angle))),
        radius = controlPoint2Radius,
        angle = controlPoint2Angle
    )



    return CubicBezierCurve(
        from = from,
        to = to,
        controlPoint1 = controlPoint1,
        controlPoint2 = controlPoint2
    )
}