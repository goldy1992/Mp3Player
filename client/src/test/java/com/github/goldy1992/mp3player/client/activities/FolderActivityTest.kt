package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

import java.io.File

@RunWith(RobolectricTestRunner::class)
class FolderActivityTest {

    private lateinit var scenario: ActivityScenario<FolderActivityInjectorTestImpl>

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, FolderActivityInjectorTestImpl::class.java)
        val folder = File("/a/b/xyz")
        val mediaItem = MediaItemBuilder("id")
                .setLibraryId("xyz")
                .setDirectoryFile(folder)
                .build()
        intent.putExtra(Constants.MEDIA_ITEM, mediaItem)
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun testOnBackPressed() {
        scenario.onActivity { activity : FolderActivity ->
            activity.onBackPressed()
            Assert.assertTrue(activity.isFinishing)
        }
        scenario.close()
    }
}