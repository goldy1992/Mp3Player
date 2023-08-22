package com.github.goldy1992.mp3player.client.utils.visualizer.circle

import androidx.compose.ui.unit.IntSize
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class MultikCircularCurveFitterTest : CircularCurveFitterTestBase() {


    @Before
    fun setUp() {
    }

    @Test
    fun testCase1() {
        val canvasSize = IntSize(600, 600)
        val curveFitter = MultikCircularCurveFitter(canvasSize)

        val frequencies = createFrequenciesTestCase1()
        val result = curveFitter.generateBeziers(frequencies)
        assertTrue(result != null)
    }
}