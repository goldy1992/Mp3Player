package com.github.goldy1992.mp3player.client.utils

import androidx.compose.ui.unit.dp
import junit.framework.Assert.assertEquals
import org.junit.Test

class EqualizerUtilsTest {

    @Test
    fun testCalculateBarWidth() {
        val containerWidth = 40.dp
        val spacing = 2.dp
        val numOfBars = 3
        val expected = 12.dp
        val result= calculateBarWidthPixels(
            containerWidth = containerWidth,
        numOfBars = numOfBars,
        spaceBetweenBars =  spacing)

        assertEquals(expected, result)
    }
}