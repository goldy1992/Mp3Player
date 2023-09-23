package com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic

import androidx.compose.ui.geometry.Offset
import com.github.goldy1992.mp3player.client.ui.components.equalizer.circular.CubicBezierCurveOffset

object BezierCurveGenerator {

    fun generateBezierList(points: List<Point>, controlPoints1 : List<Point>, controlPoints2: List<Point>) : List<CubicBezierCurveOffset>{
        var cp1: Point
        var cp2: Point
        var end: Point
        val beziers = mutableListOf<CubicBezierCurveOffset>()
        for (i in 1 until  controlPoints1.size) {
            cp1 = controlPoints1[i-1]
            cp2 = controlPoints2[i]
            end = points[i]

            val endOffset = Offset(end.X.toFloat(), end.Y.toFloat())
            val cp1Offset = Offset(cp1.X.toFloat(), cp1.Y.toFloat())
            val cp2Offset = Offset(cp2.X.toFloat(), cp2.Y.toFloat())
            beziers.add(CubicBezierCurveOffset(to = endOffset, controlPoint1 = cp1Offset, controlPoint2 = cp2Offset))
        }
        end = points[0]
        cp1 = controlPoints1[controlPoints1.size - 1]
        cp2 = controlPoints2[0]
        val endOffset = Offset(end.X.toFloat(), end.Y.toFloat())
        val cp1Offset = Offset(cp1.X.toFloat(), cp1.Y.toFloat())
        val cp2Offset = Offset(cp2.X.toFloat(), cp2.Y.toFloat())

        beziers.add(
            CubicBezierCurveOffset(
            controlPoint1 = cp1Offset, controlPoint2 = cp2Offset, to = endOffset
        )
        )
        return beziers
    }
}