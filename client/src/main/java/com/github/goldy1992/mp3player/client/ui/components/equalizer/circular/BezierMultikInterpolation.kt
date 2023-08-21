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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import com.github.goldy1992.mp3player.client.ui.components.equalizer.circular.CubicBezierCurveOffset
import org.jetbrains.kotlinx.multik.api.identity
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.api.toNDArray
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.MutableMultiArray
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.div
import org.jetbrains.kotlinx.multik.ndarray.operations.minus
import org.jetbrains.kotlinx.multik.ndarray.operations.plus
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import org.jetbrains.kotlinx.multik.ndarray.operations.toList
import org.jetbrains.kotlinx.multik.ndarray.operations.toMutableList
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val LOG_TAG = "MultikCurve"

fun curveMultik(points: D2Array<Double>) {
    val numberOfCurves = points.shape[0] - 1
    val identity : MutableMultiArray<Double, D2> = mk.identity<Double>(numberOfCurves).times(4.0)
    val coefficientMatrix = fillDiagonal(identity)

    println(identity)

    val P = fillPointsVector(points)
    val A = mk.linalg.solve(coefficientMatrix, P)
    val B = mk.zeros<Double>(numberOfCurves, 2)
    for (i in 0 until  numberOfCurves - 1) {
        B[i] = points[i + 1].times(2.0) - A[i + 1]
    }
    B[numberOfCurves - 1] = (A[numberOfCurves - 1] + points[numberOfCurves]) / 2.0




}

fun generateMultikBeziers(points: D2Array<Double>) : List<CubicBezierCurveOffset> {
    val numberOfCurves = points.shape[0] - 1
    val identity : MutableMultiArray<Double, D2> = mk.identity<Double>(numberOfCurves).times(4.0)
    val coefficientMatrix = fillDiagonal(identity)

    println(identity)

    val P = fillPointsVector(points)
    val A = mk.linalg.solve(coefficientMatrix, P)
    val B = mk.zeros<Double>(numberOfCurves, 2)
    for (i in 0 until  numberOfCurves - 1) {
        B[i] = points[i + 1].times(2.0) - A[i + 1]
    }
    B[numberOfCurves - 1] = (A[numberOfCurves - 1] + points[numberOfCurves]) / 2.0

    Log.v(LOG_TAG, "A: $A")
    Log.v(LOG_TAG,"B: $B")

    val beziers = mutableListOf<CubicBezierCurveOffset>()
    for (i in 0 until  numberOfCurves) {
        val start = points[i]
        val startPoint = Offset(start[0].toFloat(), start[1].toFloat())
        val end = points[i+1]
        val endPoint = Offset(end[0].toFloat(), end[1].toFloat())
        val cp1 = Offset(A[i][0].toFloat(), A[i][1].toFloat())
        val cp2 = Offset(B[i][0].toFloat(), B[i][1].toFloat())
        beziers.add(CubicBezierCurveOffset(from = startPoint, to = endPoint, controlPoint1 = cp1, controlPoint2 = cp2))
    }
    return beziers
}


private fun fillDiagonal(sqMatrix: MutableMultiArray<Double, D2> ) : MutableMultiArray<Double, D2> {
    val length  = sqMatrix.shape[0]
    val lastElement  = length - 1
    for (n in 0 .. lastElement) {
        val currentRow = sqMatrix[n].toMutableList()

        if (n == 0) {
            currentRow[0] = 2.0
        }

        val idxLeft = n-1
        if (idxLeft in 0 .. lastElement) {
            currentRow[idxLeft] = 1.0
        }
        val idxRight = n + 1
        if (idxRight in 0 .. lastElement) {
            currentRow[idxRight] = 1.0
        }

        else if (n == lastElement) {
            currentRow[lastElement] = 7.0
            currentRow[lastElement - 1] = 2.0

        }

        sqMatrix[n] = currentRow.toNDArray()

    }
    return sqMatrix
}

private fun fillPointsVector(points: D2Array<Double>) : D2Array<Double> {

    val len = points.shape[0] - 1
    val pMatrix = mutableListOf<List<Double>>()
    for (i in 0 until  len) {
        val p = (points[i].times(2.0) + points[i+1]).times(2.0)
        pMatrix.add(p.toList())
    }

//    points.mapIndexed { idx, v -> v.times(2.0) + points[idx+1].times(2.0) }.toCollection()
    pMatrix[0] = (points[0] + (2.0 * points[1])).toList()
    pMatrix[len-1] = (points[len-1].times(8.0) + points[len]).toList()

   // mk.ndarray(pMatrix)
    return mk.ndarray(pMatrix)//>() mk.ndarray(mk[pMatrix])
    /*
    * # build points vector
	P = [2 * (2 * points[i] + points[i + 1]) for i in range(n)]
	P[0] = points[0] + 2 * points[1]
	P[n - 1] = 8 * points[n - 1] + points[n]

    *  */
}


private fun createPoints() : D2Array<Double> {
    // use (x - o)^2 + (y-o)^2 = r^2
    val offset = 300f
    val r = 200f
    val rSq = r.pow(2)
    val points = mutableListOf<List<Double>>()

    for (n in -200 .. 200 step 20) {
        // y = sqrt(r^2 - x^2)
        val x = (n + offset).toDouble()
        val y = sqrt(rSq - (n.toFloat().pow(2))).toDouble() + offset
        val point = mk[x, y]
        points.add(point)
   }

    return mk.ndarray(points)
}

private fun createRadialPoints(
    offset : Offset = Offset(300f, 300f),
    radius : Float = 200f,
    n : Int = 15
) : D2Array<Double> {
    val points = mutableListOf<List<Double>>()

    val step = (2 * Math.PI) / n
    var feta = 0.0
    while (feta < (2*Math.PI)) {
        val x = offset.x + (radius * cos((feta)))
        val y = offset.y + (radius * sin((feta)))
        val point = mk[x, y]
        points.add(point)
        feta += step
    }

    return mk.ndarray(points)
}

fun main() {
    val points = createPoints()
    curveMultik(points)
}

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
    Log.v(LOG_TAG, "Invoked: CircleEqualizerUsingMulkit")
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





    val radius = 200f
    val offset = Offset(300f, 300f)
    val points = createRadialPoints(radius = radius, offset = offset)
    val beziers = generateMultikBeziers(frequencyCoordinates)

    Canvas(modifier = modifier.fillMaxSize()) {
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
