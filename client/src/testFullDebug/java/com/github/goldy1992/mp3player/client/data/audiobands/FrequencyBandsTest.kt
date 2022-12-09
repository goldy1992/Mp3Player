package com.github.goldy1992.mp3player.client.data.audiobands

import org.junit.Assert.*
import org.junit.Test

class FrequencyBandsTest {

    @Test
    fun testFrequencyBandFive() {
        val frequencyBandFive = FrequencyBandFive()
        val result = frequencyBandFive.frequencyBands()
        assertEquals(5, result.size)
    }

    @Test
    fun testFrequencyBandTwentyFour() {
        val frequencyBandTwentyFour = FrequencyBandTwentyFour()
        val result = frequencyBandTwentyFour.frequencyBands()
        assertEquals(24, result.size)
    }

    @Test
    fun testFrequencyBandThirtyOne() {
        val frequencyBandThirtyOne = FrequencyBandThirtyOne()
        val result = frequencyBandThirtyOne.frequencyBands()
        assertEquals(31, result.size)
    }
}