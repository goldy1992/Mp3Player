package com.github.goldy1992.mp3player.client.ui;

import org.mockito.kotlin.mock
import androidx.navigation.NavController;
import com.github.goldy1992.mp3player.client.ui.components.equalizer.VisualizerType
import com.github.goldy1992.mp3player.commons.Screen
import org.junit.Assert.assertEquals

import org.junit.Test;
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class NavigationUtilsTest {

    private val mockNavController :NavController = mock()





    @Test
    fun testNavigateVisualizer() {
        val expectedVisualizerType = VisualizerType.BAR

        argumentCaptor<String>().apply {
            NavigationUtils.navigate(mockNavController, expectedVisualizerType)
            verify(mockNavController, times(1)).navigate(this.capture(), eq(null), eq(null))

            val result = this.firstValue
            val elements = result.split("/")
            assertEquals(Screen.SINGLE_VISUALIZER.name, elements[0])
            assertEquals(expectedVisualizerType.name, elements[1])
        }
    }

}