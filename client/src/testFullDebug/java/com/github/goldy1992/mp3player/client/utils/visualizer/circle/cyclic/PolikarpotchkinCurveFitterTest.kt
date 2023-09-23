package com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.CircularCurveFitter
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.cos
import kotlin.math.sin

/**
 * Test class for the [PolikarpotchkinCurveFitter].
 */
class PolikarpotchkinCurveFitterTest {

    lateinit var curveFitter : CircularCurveFitter

    /**
     * GIVEN: 5 frequencies of minimum value 0
     * WHEN: The [PolikarpotchkinCurveFitter] processes the frequencies.
     * THEN: They lie as equally spaced points on the edge circle of the minimum radius relative to
     * the minimum width/height of the canvas.
     */
    @Test
    fun testZeroFrequencies() {
        val delta = 0.1f // accuracy +/- 0.1
        val length = 1000
        val numberOfFrequencies = 5
        val minRadiusScaleFactor = 0.4f
        val expectedCenter = Offset(length / 2f, length / 2f)
        val inset = 50f
        curveFitter = PolikarpotchkinCurveFitter(
            canvasSize = IntSize(length, length),
            insetPx = inset,
            minRadiusScaleFactor = minRadiusScaleFactor
        )

        val testData = (1..numberOfFrequencies).map { 0f }.toList()
        val result = curveFitter.generateBeziers(testData)
        assertEquals(numberOfFrequencies, result.size)
        val spacing = (2* Math.PI) / numberOfFrequencies
        var currentAngle = spacing

        // expectation

        val expectedMinRadius = ((length - (2 * inset)) * minRadiusScaleFactor) / 2.0f

        for (p in 0 until numberOfFrequencies) {
            val pointP = result[p].to
            val expectedPoint0X = (expectedMinRadius * cos(currentAngle).toFloat()) + expectedCenter.x
            assertEquals(expectedPoint0X, pointP.x, delta)
            val expectedPoint0Y = expectedMinRadius * sin(currentAngle).toFloat() + expectedCenter.y
            assertEquals(expectedPoint0Y, pointP.y, delta)
            currentAngle += spacing
        }
    }
}