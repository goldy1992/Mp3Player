package com.github.goldy1992.mp3player.client.ui.components.equalizer.circular

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
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.LUDecomposition
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import kotlin.math.cos
import kotlin.math.sin


fun CreateApache() {

}

private fun createPoints(
        offset : Offset = Offset(300f, 300f),
        radius : Float = 200f,
        n : Int = 15
    ) : RealMatrix {
        val points = mutableListOf<DoubleArray>()

        val step = (2 * Math.PI) / n
        var feta = 0.0
        while (feta < (2*Math.PI)) {
            val x = offset.x + (radius * cos((feta)))
            val y = offset.y + (radius * sin((feta)))
            val point = doubleArrayOf(x, y)
            points.add(point)
            feta += step
        }
        return MatrixUtils.createRealMatrix(points.toTypedArray())
}


private fun fillDiagonal(sqMatrix: RealMatrix ) : RealMatrix{
    if (!sqMatrix.isSquare) {
        print("isNotSq")
    }
    val length  = sqMatrix.columnDimension
    val lastElement  = length - 1
    for (n in 0 .. lastElement) {
        val currentRow = sqMatrix.getRow(n)

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

        //sqMatrix[n] = currentRow.toNDArray()

    }
    return sqMatrix
}


private fun fillPointsVector(points: RealMatrix) : RealMatrix {

    val len = points.rowDimension - 1
    val pMatrix = MatrixUtils.createRealMatrix(len, 2)
    for (i in 0 until  len) {
        val p = points.getRowVector(i).mapMultiply(2.0).add(points.getRowVector(i+1)).mapMultiply(2.0)
        pMatrix.setRowVector(i, p)
    }

    pMatrix.setRowVector(0,  points.getRowVector(0).add(points.getRowVector(1).mapMultiply(2.0)))
    pMatrix.setRowVector(len-1, points.getRowVector(len-1).mapMultiply(8.0).add(points.getRowVector(len)))

    return pMatrix
    /*
    * # build points vector
	P = [2 * (2 * points[i] + points[i + 1]) for i in range(n)]
	P[0] = points[0] + 2 * points[1]
	P[n - 1] = 8 * points[n - 1] + points[n]

    *  */
}


fun generateApacheBeziers(points: RealMatrix) : List<CubicBezierCurveOffset> {
    val numberOfCurves = points.rowDimension - 1
    val identity = MatrixUtils.createRealIdentityMatrix(numberOfCurves).scalarMultiply(4.0)
    val coefficientMatrix = fillDiagonal(identity)

    println(identity)

    val P = fillPointsVector(points)
    val solver = LUDecomposition(coefficientMatrix).solver
    val A = solver.solve(P)
    val B = MatrixUtils.createRealMatrix(numberOfCurves, 2)

    for (i in 0 until  numberOfCurves - 1) {
        B.setRowVector(i,

            points.getRowVector(i + 1).mapMultiply(2.0).subtract(A.getRowVector(i + 1) ))

    }
    B.setRowVector(numberOfCurves - 1,
    A.getRowVector(numberOfCurves - 1).add(points.getRowVector(numberOfCurves)).mapDivide(2.0))
//
//    Log.v(LOG_TAG, "A: $A")
//    Log.v(LOG_TAG,"B: $B")

    val beziers = mutableListOf<CubicBezierCurveOffset>()
    for (i in 0 until  numberOfCurves) {
        val start = points.getRowVector(i)
        val startPoint = Offset(start.getEntry(0).toFloat(), start.getEntry(1).toFloat())
        val end = points.getRowVector(i+1)
        val endPoint = Offset(end.getEntry(0).toFloat(), end.getEntry(1).toFloat())
        val cp1 = Offset(A.getRowVector(i).getEntry(0).toFloat(), A.getRowVector(i).getEntry(1).toFloat())
        val cp2 = Offset(B.getRowVector(i).getEntry(0).toFloat(), B.getRowVector(i).getEntry(1).toFloat())
        beziers.add(CubicBezierCurveOffset(from = startPoint, to = endPoint, controlPoint1 = cp1, controlPoint2 = cp2))
    }
    return beziers
}

fun main() {
    val points = createPoints()
    generateApacheBeziers(points)
}


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
    //Log.v(LOG_TAG, "Invoked: CircleEqualizerUsingMulkit")
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

    val beziers = generateApacheBeziers(frequencyCoordinates)

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


private fun offsetCoordinates(frequencies: List<Float>, minRadius : Float, center : Offset) : RealMatrix {

    val maxFreqRadius = 150.0
    val maxFreq = 300

    var angle = 0.0
    return if (frequencies.isNotEmpty()) {
        val toReturn = MatrixUtils.createRealMatrix(frequencies.size + 1, 2)
        val spacing = ((2 * Math.PI) / frequencies.count()).toFloat()

        frequencies.forEachIndexed { index, frequency ->
            val radius = minRadius + (maxFreqRadius * (frequency / maxFreq))
            val x = (radius * cos(angle)) + center.x
            val y = (radius * sin(angle)) + center.y
            val v = ArrayRealVector(doubleArrayOf( x, y))
            toReturn.setRowVector(index, v)
            angle += spacing
        }
       toReturn.setRowVector(frequencies.size, toReturn.getRowVector(0))
        toReturn
    } else {
        val toReturn = MatrixUtils.createRealMatrix(16, 2)
        val spacing = ((2 * Math.PI) / 15)
        for (i in 0 until 15) {
            val x = (minRadius * cos(angle)) + center.x
            val y = (minRadius * sin(angle)) + center.y
            val v = ArrayRealVector(doubleArrayOf( x, y))
            toReturn.setRowVector(i, v)
            angle += spacing
        }
        toReturn.setRowVector(15, toReturn.getRowVector(0))
        toReturn
    }



}

