package com.github.goldy1992.mp3player.client.models.visualizers.fountainspring

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.pow

/**
 * Test class for [Particle].
 */
class ParticleTest {

    @Test
    fun createParticle() {
        val startOffset = Offset(100f, 0f)
        val initialVelocity = 10f
        val hMax = 200f
        val color = Color.Blue
        val result = Particle.createParticle(startOffset, initialVelocity, hMax, color)
        assertEquals(100f, result.x)
        assertEquals(0f, result.y)
        assertEquals(color, result.color)
        assertEquals(hMax, result.hMax)
        assertEquals(initialVelocity, result.initialVelocity)
    }

    /**
     * When the elapsed time is greater than the time needed to reach hMax then the particle
     * should show as falling
     */
    @Test
    fun testParticleUpdateParticleFalling() {
        val startOffset = Offset(100f, 0f)
        val initialVelocity = -100f
        val hMax = -506f
        val color = Color.Blue
        val particle = Particle(
            startPosition = startOffset,
            initialVelocity = initialVelocity,
            angle = 85.0,
            hMax = hMax,
            isFalling = false,
            color = color,
            elapsedTimeDeciSeconds = 25f
        )

        val elapsedTime = 2L * 10.0.pow(8.0).toLong()// 2 seconds
        val result = particle.update(elapsedTime)
        assertTrue(result.isFalling)
    }

    /**
     * When the elapsed time is less than the time needed to reach hMax then the particle
     * should show as NOT falling
     */
    @Test
    fun testParticleUpdateParticleNotFalling() {
        val startOffset = Offset(100f, 0f)
        val initialVelocity = -100f
        val hMax = -506f
        val color = Color.Blue
        val particle = Particle(
            startPosition = startOffset,
            initialVelocity = initialVelocity,
            angle = 85.0,
            hMax = hMax,
            isFalling = false,
            color = color,
            elapsedTimeDeciSeconds = 5f
        )

        val elapsedTime = 2L * 10.0.pow(8.0).toLong()// 2 seconds
        val result = particle.update(elapsedTime)
        assertFalse(result.isFalling)
    }

}