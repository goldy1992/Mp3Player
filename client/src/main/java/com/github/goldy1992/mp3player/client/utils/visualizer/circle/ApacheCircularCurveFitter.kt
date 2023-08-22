package com.github.goldy1992.mp3player.client.utils.visualizer.circle

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.github.goldy1992.mp3player.client.ui.components.equalizer.circular.CubicBezierCurveOffset
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.LUDecomposition
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix

class ApacheCircularCurveFitter(canvasSize: IntSize,
    override val insetPx: Float = 15f,
override val maxFreqRadius: Double = 150.0,
override val maxFreq: Double = 300.0) : CircularCurveFitter {

    override val center: Offset = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
    override val minRadius: Float =  (0.9f * minOf(canvasSize.width - (2 * insetPx), canvasSize.height - (2 * insetPx)))  / 2.1f
    override fun generateBeziers(frequencies: List<Float>): List<CubicBezierCurveOffset> {
        val offsetFrequencies = offsetCoordinates(frequencies)
        val points = createPointsMatrix(offsetFrequencies)
        val numberOfCurves = points.rowDimension - 1
        val identity = MatrixUtils.createRealIdentityMatrix(numberOfCurves).scalarMultiply(4.0)
        val coefficientMatrix = fillDiagonal(identity)

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

    override fun getType(): String {
        return "ApacheMath"
    }

    override fun logTag(): String {
        return "ApacheCircularFitter"
    }


    private fun createPointsMatrix(offsets : List<Offset>) : RealMatrix {
        val toReturn = MatrixUtils.createRealMatrix(offsets.size + 1, 2)
        offsets.forEachIndexed { index, offset ->
            val v = ArrayRealVector(doubleArrayOf( offset.x.toDouble(), offset.y.toDouble()))
            toReturn.setRowVector(index, v)
        }
        return toReturn
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

}