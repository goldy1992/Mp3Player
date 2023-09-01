package com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.github.goldy1992.mp3player.client.ui.components.equalizer.circular.CubicBezierCurveOffset
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.CircularCurveFitter
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic.BezierCurveGenerator.generateBezierList

class PolikarpotchkinCurveFitter(
    canvasSize: IntSize,
    override val insetPx: Float = 15f,
    override val maxFreqRadius: Double = 150.0,
    override val maxFreq: Double = 300.0,
) : CircularCurveFitter {

    override val center: Offset = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
    override val minRadius: Float = (0.9f * minOf(canvasSize.width - (2 * insetPx), canvasSize.height - (2 * insetPx)))  / 2.1f

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