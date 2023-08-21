package com.github.goldy1992.mp3player.client.utils.visualizer.circle

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
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

class MultikCircularCurveFitter(
    canvasSize: IntSize,
    override val insetPx: Float = 15f,
    override val maxFreqRadius: Double = 150.0,
    override val maxFreq: Double = 300.0) : CircularCurveFitter {

    override val center: Offset = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
    override val minRadius: Float =  (0.9f * minOf(canvasSize.width - (2 * insetPx), canvasSize.height - (2 * insetPx)))  / 2.1f


    override fun generateBeziers(
        frequencies: List<Float>): List<CubicBezierCurveOffset> {
        val offsetFrequencies = offsetCoordinates(frequencies)
        val points = offsetFrequencies.map { mk[it.x, it.y] }.toNDArray()
        val numberOfCurves = points.shape[0] - 1
        val identity : MutableMultiArray<Float, D2> = mk.identity<Float>(numberOfCurves).times(4f)
        val coefficientMatrix = fillDiagonal(identity)

        val P = fillPointsVector(points)
        val A = mk.linalg.solve(coefficientMatrix, P)
        val B = mk.zeros<Float>(numberOfCurves, 2)
        for (i in 0 until  numberOfCurves - 1) {
            B[i] = points[i + 1].times(2f) - A[i + 1]
        }
        B[numberOfCurves - 1] = (A[numberOfCurves - 1] + points[numberOfCurves]) / 2.0f

        Log.v(logTag(), "A: $A")
        Log.v(logTag(),"B: $B")

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


    private fun fillDiagonal(sqMatrix: MutableMultiArray<Float, D2> ) : MutableMultiArray<Float, D2> {
        val length  = sqMatrix.shape[0]
        val lastElement  = length - 1
        for (n in 0 .. lastElement) {
            val currentRow = sqMatrix[n].toMutableList()

            if (n == 0) {
                currentRow[0] = 2.0f
            }

            val idxLeft = n-1
            if (idxLeft in 0 .. lastElement) {
                currentRow[idxLeft] = 1.0f
            }
            val idxRight = n + 1
            if (idxRight in 0 .. lastElement) {
                currentRow[idxRight] = 1.0f
            }

            else if (n == lastElement) {
                currentRow[lastElement] = 7.0f
                currentRow[lastElement - 1] = 2.0f

            }

            sqMatrix[n] = currentRow.toNDArray()

        }
        return sqMatrix
    }


    private fun fillPointsVector(points: D2Array<Float>) : D2Array<Float> {

        val len = points.shape[0] - 1
        val pMatrix = mutableListOf<List<Float>>()
        for (i in 0 until  len) {
            val p = (points[i].times(2.0f) + points[i+1]).times(2.0f)
            pMatrix.add(p.toList())
        }

        pMatrix[0] = (points[0] + (2.0f * points[1])).toList()
        pMatrix[len-1] = (points[len-1].times(8.0f) + points[len]).toList()
        return mk.ndarray(pMatrix)
    }



    override fun getType(): String {
        return "Multik"
    }

    override fun logTag(): String {
        return "MultikCircularFitter"
    }
}