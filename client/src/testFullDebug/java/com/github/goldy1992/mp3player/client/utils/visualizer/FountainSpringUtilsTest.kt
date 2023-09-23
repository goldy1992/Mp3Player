package com.github.goldy1992.mp3player.client.utils.visualizer

import com.github.goldy1992.mp3player.client.utils.visualizer.FountainSpringUtils.generateSpringSpawnPoints
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test class for the [FountainSpringUtils].
 */
class FountainSpringUtilsTest {

    /**
     * GIVEN: hmax = 10, angle = [Math.PI] / 2
     * WHEN: u is calculated
     * THEN: Result is -14
     */
    @Test
    fun testCalculateInitialVelocityGivenHMax() {
        val hMax = 10f
        val angle = Math.PI / 2f
        val expectedResult = -14.0f
        val result = FountainSpringUtils.calculateInitialVelocityGivenHMax(hMax, angle)
        assertEquals(expectedResult, result)
    }

    /**
     * Springs should be equally spaced across the canvas.
     * Canvas spawns should be set up as follows
     * | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 |
     * | I | _ | _ | S | _ | _ | S | _ | _ | I |
     * where:
     * - I is the inset
     * - S is the sprint point
     * They should spawn at the bottom on the canvas (i.e. the canvas height)
     */
    @Test
    fun testGenerateSpringSpawnPoints() {
        val canvasWidth = 10f
        val canvasHeight = 20f
        val insetPx = 1f
        val particleWidth = 1f
        val numberOfFrequencies = 2
        val result = generateSpringSpawnPoints(canvasWidth, canvasHeight, insetPx, particleWidth, numberOfFrequencies)
        assertEquals(2, result.size)
        val spring1 = result[0]
        assertEquals(canvasHeight, spring1.y)
        assertEquals(4.0f, spring1.x)
        val spring2 = result[1]
        assertEquals(7.0f, spring2.x)
        assertEquals(canvasHeight, spring2.y)
    }
}