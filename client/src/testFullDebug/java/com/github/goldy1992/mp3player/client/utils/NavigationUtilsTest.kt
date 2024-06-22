package com.github.goldy1992.mp3player.client.utils;

import android.net.Uri
import android.util.Base64
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.ui.components.equalizer.VisualizerType
import com.github.goldy1992.mp3player.commons.Screen
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

/**
 * Test class for [NavigationUtils].
 */
@RunWith(RobolectricTestRunner::class)
class NavigationUtilsTest {

    private val mockNavController :NavController = mock()

    @Test
    fun testNavigateAlbum() {
        val expectedAlbumId = "exAlbumId"
        val expectedAlbumTitle = "exAlbumTitle"
        val expectedAlbumArtist = "exAlbumArtist"
        val expectedUri = Uri.parse("file://test_uri")
        val album = Album(
            id = expectedAlbumId,
            title = expectedAlbumTitle,
            artist = expectedAlbumArtist,
            artworkUri = expectedUri
        )

        argumentCaptor<String>().apply {
            NavigationUtils.navigate(mockNavController, album)
            verify(mockNavController, times(1)).navigate(this.capture(), eq(null), eq(null))

            val result = this.firstValue
            val elements = result.split("/")
            assertEquals(Screen.ALBUM.name, elements[0])
            assertEquals(expectedAlbumId, elements[1])
            assertEquals(expectedAlbumTitle, elements[2])
            assertEquals(expectedAlbumArtist, elements[3])

            val resultUri = Uri.parse(String(Base64.decode(elements[4], Base64.DEFAULT)))
            assertEquals(expectedUri, resultUri)
        }
    }


    @Test
    fun testNavigateVisualizer() {
        val expectedVisualizerType = VisualizerType.BAR

        argumentCaptor<String>().apply {
            NavigationUtils.navigate(mockNavController, expectedVisualizerType, listOf(0.05f))
            verify(mockNavController, times(1)).navigate(this.capture(), eq(null), eq(null))

            val result = this.firstValue
            val elements = result.split("/")
            assertEquals(Screen.SINGLE_VISUALIZER.name, elements[0])
            assertEquals(expectedVisualizerType.name, elements[1])
        }
    }

}