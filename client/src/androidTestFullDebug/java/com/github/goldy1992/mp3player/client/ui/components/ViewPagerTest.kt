package com.github.goldy1992.mp3player.client.ui.components

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.tooling.preview.Preview
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.models.Queue
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.test.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalFoundationApi
class ViewPagerTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()


    val context = InstrumentationRegistry.getInstrumentation().context

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testDisplay() {
        composeTestRule.setContent {
            Column(Modifier.fillMaxSize()) {
                ViewPagerTestImpl()

            }

        }
        runBlocking {
            composeTestRule.waitUntilExactlyOneExists(hasContentDescription("Album Art"))
            delay(5000L)
        }

        println()
    }

    @Composable
    @Preview
    private fun ViewPagerTestImpl() {

       val queue = Queue(createSongs(), 0)
        ViewPager(
            currentSongProvider = {queue.items[0]},
            queue = queue)
    }

    private fun createSongs() : List<Song> {

        val song = Song(
            id = "1",
            title = "test_title_1",
            albumArt = getTestUri()        )

        return listOf(song)
    }

    private fun getTestUri() : Uri {

        val drawableId = R.drawable.test_album_art
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId))
    }
}