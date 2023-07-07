package com.github.goldy1992.mp3player.client.ui.components

import android.net.Uri
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.platform.app.InstrumentationRegistry
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.request.ErrorResult
import coil.test.FakeImageLoaderEngine
import org.junit.Rule
import org.junit.Test

@ExperimentalCoilApi
class AlbumArtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testUri = Uri.parse("file://www.example.com/image.jpg")

    @Test
    fun assertErrorIconIsDisplayedOnError() {
        createImageLoader { chain ->
            ErrorResult(null, chain.request, NullPointerException())
        }

        composeTestRule.setContent {
            AlbumArtAsync(uri = testUri, contentDescription = "")
        }
        composeTestRule.onNodeWithContentDescription("error-").assertExists()
    }

    @Test
    fun assertLoadingIconIsDisplayedWhenLoading() {
        createImageLoader { chain ->  chain.proceed(chain.request) }

        composeTestRule.setContent {
            AlbumArtAsync(uri = testUri, contentDescription = "")
        }
        composeTestRule.onNodeWithContentDescription("loading-").assertExists()
    }

    private fun createImageLoader(interceptor: FakeImageLoaderEngine.OptionalInterceptor) {
        val engine = FakeImageLoaderEngine.Builder()
            .addInterceptor(interceptor)
            .build()
        val imageLoader = ImageLoader.Builder(InstrumentationRegistry.getInstrumentation().context)
            .components { add(engine) }
            .build()
        Coil.setImageLoader(imageLoader)
    }
}