package com.github.goldy1992.mp3player.client.ui.components.equalizer.circular

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope

@Composable
@Preview(widthDp = 500, heightDp = 1000)
fun CircleEqualizerUsingPolikarpotchkin(
    modifier: Modifier = Modifier,
    frequencyPhasesState: () -> List<Float> = { listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f) },
    insetPx: Float = 30f,
    surfaceColor: Color = MaterialTheme.colorScheme.primaryContainer,
    lineColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    scope : CoroutineScope = rememberCoroutineScope(),
    circleVisualizerState : CircleVisualizerState = rememberCircleVisualizerState(scope, insetPx)
) {

    val frequencyPhases = frequencyPhasesState()
    val animatedFrequencies = mutableListOf<Float>()
    frequencyPhases.forEachIndexed {
            idx, frequency ->
        val animatedFrequency by animateFloatAsState(targetValue = frequency, label = "frequency[$idx]")
        animatedFrequencies.add(animatedFrequency)
    }
    circleVisualizerState.setAnimatedFrequencies(animatedFrequencies)

    val beziers by circleVisualizerState.bezierCurveState.collectAsState()

    Canvas(modifier = modifier
        .fillMaxSize()
        .onSizeChanged {
            circleVisualizerState.setSize(it)
        }) {
        if (beziers.isNotEmpty()) {
            val lastBezier = beziers[beziers.size - 1]
            val p = Path().apply {
                moveTo(x = lastBezier.to.x, y = lastBezier.to.y)
                for (b in beziers) {
                    cubicTo(
                        b.controlPoint1.x, b.controlPoint1.y,
                        b.controlPoint2.x, b.controlPoint2.y,
                        b.to.x, b.to.y
                    )
                }
            }

            for (point in beziers) {
                drawLine(lineColor, start = center, end = point.to)
            }

            drawPath(
                p, lineColor,
                style = Stroke(
                    width = 5f,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}

