package com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.cos
import kotlin.math.sin

/**
 * Test class for constructing a Closed Bezier Spline
 */
class ClosedBezierSplineTest {

    /**
     * GIVEN: 7 points on a circle of radius 100
     * WHEN: A closed bezier spline is constructed.
     * THEN: The control points of the bezier curve are as expected.
     */
    @Test
    fun testPointsOnACircle() {
        // GIVEN
        val points = createPoints(numberOfPoints = 7, radius =  100.0)
        val delta = 0.1 // accuracy +/- 0.1

        // assert points
        assertEquals(100.0, points[0].X, delta)
        assertEquals(200.0, points[0].Y, delta)

        assertEquals(178.2, points[1].X, delta)
        assertEquals(162.3, points[1].Y, delta)

        assertEquals(197.5, points[2].X, delta)
        assertEquals(77.7, points[2].Y, delta)

        assertEquals(143.4, points[3].X, delta)
        assertEquals(9.9, points[3].Y, delta)

        assertEquals(56.6, points[4].X, delta)
        assertEquals(9.9, points[4].Y, delta)

        assertEquals(2.5, points[5].X, delta)
        assertEquals(77.7, points[5].Y, delta)

        assertEquals(21.8, points[6].X, delta)
        assertEquals(162.3, points[6].Y, delta)



        val result = ClosedBezierSpline.getCurveControlPoints(points.toTypedArray())
        assertEquals(7, result.first.size)
        assertEquals(7, result.second.size)

        val controlPoints1 = result.first
        // assert control points 1
        assertEquals(129.8, controlPoints1[0].X, delta)
        assertEquals(200.0, controlPoints1[0].Y, delta)

        assertEquals(196.8, controlPoints1[1].X, delta)
        assertEquals(139.0, controlPoints1[1].Y, delta)

        assertEquals(190.9, controlPoints1[2].X, delta)
        assertEquals(48.7, controlPoints1[2].Y, delta)

        assertEquals(116.5, controlPoints1[3].X, delta)
        assertEquals(-3.0, controlPoints1[3].Y, delta)

        assertEquals(29.8, controlPoints1[4].X, delta)
        assertEquals(22.8, controlPoints1[4].Y, delta)

        assertEquals(-4.1, controlPoints1[5].X, delta)
        assertEquals(106.8, controlPoints1[5].Y, delta)

        assertEquals(40.4, controlPoints1[6].X, delta)
        assertEquals(185.6, controlPoints1[6].Y, delta)
        
        val controlPoints2 = result.second
        // assert control points 2
        assertEquals(70.2, controlPoints2[0].X, delta)
        assertEquals(200.0, controlPoints2[0].Y, delta)

        assertEquals(159.6, controlPoints2[1].X, delta)
        assertEquals(185.6, controlPoints2[1].Y, delta)

        assertEquals(204.1, controlPoints2[2].X, delta)
        assertEquals(106.8, controlPoints2[2].Y, delta)

        assertEquals(170.2, controlPoints2[3].X, delta)
        assertEquals(22.8, controlPoints2[3].Y, delta)

        assertEquals(83.5, controlPoints2[4].X, delta)
        assertEquals(-3.0, controlPoints2[4].Y, delta)

        assertEquals(9.1, controlPoints2[5].X, delta)
        assertEquals(48.7, controlPoints2[5].Y, delta)

        assertEquals(3.2, controlPoints2[6].X, delta)
        assertEquals(139.0, controlPoints2[6].Y, delta)

        val deltaf = 0.1f
        val curves = BezierCurveGenerator.generateBezierList(points, controlPoints1.toList(), controlPoints2.toList())
        assertEquals(curves.size, points.size)
        val curve0 = curves[0]
        assertEquals(129.8f, curve0.controlPoint1.x, deltaf)
        assertEquals(200.0f, curve0.controlPoint1.y, deltaf)
        assertEquals(159.6f, curve0.controlPoint2.x, deltaf)
        assertEquals(185.6f, curve0.controlPoint2.y, deltaf)
        assertEquals(178.2f, curve0.to.x, deltaf)
        assertEquals(162.3f, curve0.to.y, deltaf)

        val curve1 = curves[1]
        assertEquals(196.8f, curve1.controlPoint1.x, deltaf)
        assertEquals(139.0f, curve1.controlPoint1.y, deltaf)
        assertEquals(204.1f, curve1.controlPoint2.x, deltaf)
        assertEquals(106.8f, curve1.controlPoint2.y, deltaf)
        assertEquals(197.5f, curve1.to.x, deltaf)
        assertEquals(77.7f, curve1.to.y, deltaf)

        val curve2 = curves[2]
        assertEquals(190.9f, curve2.controlPoint1.x, deltaf)
        assertEquals(48.7f, curve2.controlPoint1.y, deltaf)
        assertEquals(170.2f, curve2.controlPoint2.x, deltaf)
        assertEquals(22.8f, curve2.controlPoint2.y, deltaf)
        assertEquals(143.4f, curve2.to.x, deltaf)
        assertEquals(9.9f, curve2.to.y, deltaf)

        val curve3 = curves[3]
        assertEquals(116.5f, curve3.controlPoint1.x, deltaf)
        assertEquals(-3.0f, curve3.controlPoint1.y, deltaf)
        assertEquals(83.5f, curve3.controlPoint2.x, deltaf)
        assertEquals(-3.0f, curve3.controlPoint2.y, deltaf)
        assertEquals(56.6f, curve3.to.x, deltaf)
        assertEquals(9.9f, curve3.to.y, deltaf)

        val curve4 = curves[4]
        assertEquals(29.8f, curve4.controlPoint1.x, deltaf)
        assertEquals(22.8f, curve4.controlPoint1.y, deltaf)
        assertEquals(9.1f, curve4.controlPoint2.x, deltaf)
        assertEquals(48.7f, curve4.controlPoint2.y, deltaf)
        assertEquals(2.5f, curve4.to.x, deltaf)
        assertEquals(77.7f, curve4.to.y, deltaf)

        val curve5 = curves[5]
        assertEquals(-4.1f, curve5.controlPoint1.x, deltaf)
        assertEquals(106.8f, curve5.controlPoint1.y, deltaf)
        assertEquals(3.2f, curve5.controlPoint2.x, deltaf)
        assertEquals(139.0f, curve5.controlPoint2.y, deltaf)
        assertEquals(21.8f, curve5.to.x, deltaf)
        assertEquals(162.3f, curve5.to.y, deltaf)

        val curve6 = curves[6]
        assertEquals(40.3f, curve6.controlPoint1.x, deltaf)
        assertEquals(185.6f, curve6.controlPoint1.y, deltaf)
        assertEquals(70.1f, curve6.controlPoint2.x, deltaf)
        assertEquals(200.0f, curve6.controlPoint2.y, deltaf)
        assertEquals(100.0f, curve6.to.x, deltaf)
        assertEquals(200.0f, curve6.to.y, deltaf)
    }

    private fun createPoints(numberOfPoints: Int = 7, radius: Double = 100.0): MutableList<Point> {
        val points = mutableListOf<Point>()
        val angleStep: Double = 2 * Math.PI / 7

        for (i in 0 until numberOfPoints) {
            val angle = i * angleStep
            points.add(
                Point(
                    radius * (sin(angle) + 1.0), radius * (cos(angle) + 1.0)
                )
            )
        }
        return points
    }
}