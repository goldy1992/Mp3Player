package com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.github.goldy1992.mp3player.client.ui.components.equalizer.circular.CubicBezierCurveOffset
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.CircularCurveFitter
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic.BezierCurveGenerator.generateBezierList

class PolikarpotchkinCurveFitter(
    canvasSize: IntSize,
    override val insetPx: Float = 15f,
    override val maxFreq: Double = 300.0,
    minRadiusScaleFactor: Float = 0.4f
) : CircularCurveFitter {
    private val minLength = minOf(canvasSize.width, canvasSize.height) - (2 * insetPx)
    override val center: Offset = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
    override val minRadius: Float = (minRadiusScaleFactor * minLength) / 2.0f
    override val maxRadiusDelta: Double = (minLength / 2.0) - minRadius

    override fun generateBeziers(frequencies: List<Float>): List<CubicBezierCurveOffset> {
        val offsetFrequencies = offsetCoordinates(frequencies)
        val points = offsetFrequencies.map { Point(it.x.toDouble(), it.y.toDouble()) }.toTypedArray()
        val result = ClosedBezierSpline.getCurveControlPoints(points)
        return generateBezierList(points.toList(), result.first.toList(), result.second.toList())
    }


    override fun getType(): String {
        TODO("Not yet implemented")
    }

    override fun logTag(): String {
        return "PolikarpotchkinCurveFitter"
    }
}