import android.util.Log
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
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.CircularCurveFitter
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.MultikCircularCurveFitter
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import kotlin.math.cos
import kotlin.math.sin

private const val LOG_TAG = "MultikCurve"

@Composable
@Preview
fun CircleEqualizerUsingMulkit(
    modifier: Modifier = Modifier,
    frequencyPhasesState: () -> List<Float> = { listOf(0f, 0f, 0f, 0f, 0f, 300f, 0f) },
    canvasSize: DpPxSize = DpPxSize.createDpPxSizeFromDp(500.dp, 1000.dp, LocalDensity.current),
    insetPx: Float = 10f,
    surfaceColor: Color = MaterialTheme.colorScheme.primaryContainer,
    lineColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,

) {
    val frequencyPhases = frequencyPhasesState()
    var size: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
    val curveFitter: CircularCurveFitter by remember(size, frequencyPhases.size) { mutableStateOf( MultikCircularCurveFitter(size)) }
    Log.v(LOG_TAG, "Invoked: CircleEqualizerUsingMulkit")

    val center = Offset(canvasSize.widthPx / 2, canvasSize.heightPx / 2)
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
        }

        drawPath(p, Color.Red,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )

        for (b in beziers) {
            drawCircle(Color.Cyan, 15f, b.from)
            drawCircle(Color.Yellow, 15f,  b.controlPoint2)
            //  drawCircle(Color.Yellow, 15f,  b.controlPoint1)
        }
    }
}


private fun offsetCoordinates(frequencies: List<Float>, minRadius : Float, center : Offset) : D2Array<Double> {

    val maxFreqRadius = 150.0
    val maxFreq = 300
    val toReturn = mutableListOf<List<Double>>()

    var angle = 0.0
    if (frequencies.isNotEmpty()) {
        val spacing = ((2 * Math.PI) / frequencies.count()).toFloat()

        for (frequency in frequencies) {
            val radius = minRadius + (maxFreqRadius * (frequency / maxFreq))
            val x = (radius * cos(angle)) + center.x
            val y = (radius * sin(angle)) + center.y

            toReturn.add(mk[x, y])
            angle += spacing
        }
        toReturn.add(toReturn[0])
    } else {
        val spacing = ((2 * Math.PI) / 15)
        for (i in 1..15) {
            val x = (minRadius * cos(angle)) + center.x
            val y = (minRadius * sin(angle)) + center.y
            toReturn.add(mk[x, y])
            angle += spacing
        }
        toReturn.add(toReturn[0])


    }


    return mk.ndarray(toReturn)
}
