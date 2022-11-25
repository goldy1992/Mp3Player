package com.github.goldy1992.mp3player.client.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class EqualizerUtilsTest {

    @Test
    fun testCalculateBarWidth() {
        val containerWidth = 38f
        val spacing = 2f
        val numOfBars = 3
        val expected = 10f
        val result= calculateBarWidthPixels(
            containerWidth = containerWidth,
        numOfBars = numOfBars,
        spaceBetweenBars =  spacing)

        assertEquals(expected, result)
    }
}