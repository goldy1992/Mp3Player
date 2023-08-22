package com.github.goldy1992.mp3player.client.ui.components.equalizer.circular

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.ApacheCircularCurveFitter
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.CircularCurveFitter

@Composable
@Preview(widthDp = 500, heightDp = 1000)
fun CircleEqualizerUsingApache(
    modifier: Modifier = Modifier,
    frequencyPhasesState: () -> List<Float> = { listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f) },
    canvasSize: DpPxSize = DpPxSize.createDpPxSizeFromDp(500.dp, 1000.dp, LocalDensity.current),
    insetPx: Float = 30f,
    surfaceColor: Color = MaterialTheme.colorScheme.primaryContainer,
    lineColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,

    ) {
    val frequencyPhases = frequencyPhasesState()
    val center = Offset(canvasSize.widthPx / 2, canvasSize.heightPx / 2)
    var size: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
    val curveFitter: CircularCurveFitter by remember(size, frequencyPhases.size) { mutableStateOf( ApacheCircularCurveFitter(size)) }
    var animatedFrequencies = mutableListOf<Float>()
    frequencyPhases.forEachIndexed {
            idx, frequency ->
        val animatedFrequency by animateFloatAsState(targetValue = frequency, label = "frequency[$idx]")
        animatedFrequencies.add(animatedFrequency)
    }


    val beziers = curveFitter.generateBeziers(animatedFrequencies)

    Canvas(modifier = modifier.fillMaxSize()
        .onSizeChanged {
            size = it
    }) {
        val p = Path().apply {
            moveTo(x = beziers[0].from.x, y = beziers[0].from.y)
            for (b in beziers) {
                cubicTo(
                    b.controlPoint1.x, b.controlPoint1.y,
                    b.controlPoint2.x, b.controlPoint2.y,
                    b.to.x, b.to.y
                )
            }
            close()
        }

        drawPath(
            p, Color.Red,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )
        drawCircle(Color.Green, 15f, center)
        for (b in beziers) {
            drawCircle(Color.Cyan, 15f, b.from)
            drawCircle(Color.Yellow, 15f,  b.controlPoint2)
          //  drawCircle(Color.Yellow, 15f,  b.controlPoint1)
        }
    }
}

