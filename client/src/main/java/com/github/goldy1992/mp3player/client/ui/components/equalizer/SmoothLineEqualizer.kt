package com.github.goldy1992.mp3player.client.ui.components.equalizer

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val AMPLITUDE = 15f

private const val logTag = "SmoothLineEqualizer"


@Composable
@Preview
fun SmoothLineEqualizer(modifier: Modifier = Modifier,
    frequencyPhases : List<Float> = emptyList(),
    lineColor : Color = MaterialTheme.colorScheme.secondary,
    insetPx : Float = 100f,
    scope : CoroutineScope = rememberCoroutineScope()) {

    val frequencyAnimatableList : SnapshotStateList<Animatable<Float, AnimationVector1D>> = remember(frequencyPhases.size) {
        mutableStateListOf<Animatable<Float, AnimationVector1D>>().apply {
            Log.i(logTag, "retrigger remember")
            for (i in frequencyPhases) add( Animatable(i)) }
    }

    var canvasSize by remember { mutableStateOf(IntSize(0, 0)) }

    val numberOfPhases : Int = frequencyPhases.size
    val phaseSpacing = remember(canvasSize, frequencyPhases.size) { (canvasSize.width - (2 * insetPx ) ) / (numberOfPhases + 1) }
    val lineHeight = remember(canvasSize) { canvasSize.height / 2 }


    val coordinates : List<Offset> =
        (frequencyPhases.indices)
        .map {
            Offset(
                x =insetPx + (phaseSpacing * it),
                y = lineHeight - (frequencyAnimatableList[it].value * AMPLITUDE)
            ) }
        .toList()

    val controlPoints1 : List<Offset> =
        (1 until coordinates.size)
        .map {
            Offset(
                x = (coordinates[it].x + coordinates[it - 1].x) / 2,
                y = coordinates[it - 1].y
            ) }
        .toList()

    val controlPoints2 : List<Offset> =
        (1 until coordinates.size)
            .map {
                Offset(
                    x = (coordinates[it].x + coordinates[it - 1].x) / 2,
                    y = coordinates[it].y
                ) }
            .toList()


    LaunchedEffect(key1 = frequencyPhases) {
        for (i in frequencyPhases.indices) {
            scope.launch { frequencyAnimatableList[i].animateTo(frequencyPhases[i], animationSpec = tween(300)) }
        }
    }


    Canvas(modifier = modifier
        .fillMaxSize()
        .onSizeChanged {
            canvasSize = it
        }) {
        val stroke = Path().apply {
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
        val fillPath = android.graphics.Path(stroke.asAndroidPath())
            .asComposePath()
            .apply {
                if (coordinates.isNotEmpty()) {
                    lineTo(coordinates.last().x, lineHeight.toFloat())
                    lineTo(0f, lineHeight.toFloat())
                    close()
                }
            }
        drawPath(
            fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Cyan,
                    Color.Transparent,
                ),
                endY = lineHeight.toFloat()
            ),
        )

        drawPath(
            stroke,
            color = lineColor,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )
    }
}


