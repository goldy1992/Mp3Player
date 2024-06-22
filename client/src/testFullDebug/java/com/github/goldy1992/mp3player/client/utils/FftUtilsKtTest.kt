package com.github.goldy1992.mp3player.client.utils

import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBandFive
import org.apache.commons.math3.complex.Complex
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.sqrt

class FftUtilsKtTest {

    /**
     * Simple test for calculating magnitude given an array of complex numbers.
     * 1+3i should have magnitude sqrt(10) i.e 1^2 + 3^2 = sqrt(10)^2
     */
    @Test
    fun testGetMagnitude() {
        val testCase = Complex(1.0, 3.0)
        val result = getMagnitude(arrayOf( testCase))
        assertEquals(sqrt(10.0), result[0], 0.001)
    }

    /**
     * Simple test for calculating phase given an array of complex numbers.
     * 1+1i should have phase  PI/4 since tan(PI/4) = 1/1 = 1
     */
    @Test
    fun testGetPhase() {
        val testCase = Complex(1.0, 1.0)
        val result = getPhase(arrayOf( testCase))
        val expected = Math.PI / 4.0
        assertEquals(expected, result[0], 0.001)
    }

    /**
     * Calculates the frequency bands for a [FrequencyBandFive] passing in an array of size 400
     * given each array value is 10.0
     * Expect the last value array value returned to be 0 since a [FrequencyBandFive] expects an
     * array of size 4001 to fulfil the last frequency band.
     */
    @Test
    fun testCalculateFrequencyBandsAveragesForFrequencyBandFive() {
        val doubleArray = Array(4000) { 10.0}
        val frequencyBandFive = FrequencyBandFive()
        val result = calculateFrequencyBandsAverages(doubleArray, frequencyBand = frequencyBandFive)
        assertEquals(5, result.size)
        val expectedArray0Size = (10*41) / 40.0f
        assertEquals(expectedArray0Size, result[0])
        // the last frequency band should be 0, since it expects index 4000 and the array size is 4000
        assertEquals(0.0f, result[4])
    }
}