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
import org.apache.commons.math3.complex.Complex
import kotlin.math.cos
import kotlin.math.sin


private const val LOG_TAG = "CircleVisualizerInscpeImpl"

@OptIn(ExperimentalTextApi::class)
@Preview(widthDp =  500, heightDp = 1000, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CircularEqualizerInscapeImpl(
    modifier: Modifier = Modifier,
    frequencyPhasesState: () -> List<Float> = { listOf(0f, 0f, 0f, 0f, 0f, 300f, 0f) },
    canvasSize: DpPxSize = DpPxSize.createDpPxSizeFromDp(500.dp, 1000.dp, LocalDensity.current),
    insetPx: Float = 10f,
    surfaceColor: Color = MaterialTheme.colorScheme.primaryContainer,
    lineColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
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

    val bezierPoints = autosmooth(
            frequencyCoordinates.map { Complex(it.offset.x.toDouble(), it.offset.y.toDouble())  }.toList()
        )

    val logStr = bezierPoints.map { "(${it.start.real}, ${it.start.imaginary})" }.joinToString { "," }
    Log.i(LOG_TAG, "points: $logStr")

    Canvas(modifier = modifier
        .fillMaxSize()

    ) {
        val curvePath = Path().apply {
            reset()
            if (frequencyCoordinates.isNotEmpty()) {
                val firstVal = bezierPoints.get(0).start
                moveTo(firstVal.real.toFloat(), firstVal.imaginary.toFloat())
                bezierPoints.forEachIndexed {
                    idx, i ->
                    if (idx != 0 && idx != bezierPoints.size -1) {
                        cubicTo(
                            i.controlPoint1.real.toFloat(), i.controlPoint1.imaginary.toFloat(),
                            i.controlPoint2.real.toFloat(), i.controlPoint2.imaginary.toFloat(),
                            i.end.real.toFloat(), i.end.imaginary.toFloat(),

                            )
                    }
                }
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
        //    drawCircle(Color.Green, radius = minRadius, center = center)
        drawPath(
            path = curvePath,
            color = Color.Red,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )



        frequencyCoordinates.forEachIndexed { index, c ->
            //   drawCircle(Color.Red, radius = 10f, center = c.offset)
            //        drawText(textMeasurer, "${c.angle}", topLeft = c.offset)
        }



    }
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
         //   Log.i(LOG_TAG, "Offset: ${offset}")
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

    Log.i(LOG_TAG, "returning: ${toReturn.size} points.")
    return toReturn.toList()
}
