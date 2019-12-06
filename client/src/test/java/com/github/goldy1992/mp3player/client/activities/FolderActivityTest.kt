package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import java.io.File

@RunWith(RobolectricTestRunner::class)
class FolderActivityTest {

    private var scenario: ActivityController<com.github.goldy1992.mp3player.client.testsupport.activities.FolderActivityInjectorTestImpl>? = null

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val intent = Intent(context, com.github.goldy1992.mp3player.client.testsupport.activities.FolderActivityInjectorTestImpl::class.java)
        val folder = File("/a/b/xyz")
        val mediaItem = MediaItemBuilder("id")
                .setLibraryId("xyz")
                .setDirectoryFile(folder)
                .build()
        intent.putExtra(Constants.MEDIA_ITEM, mediaItem)
        scenario = Robolectric.buildActivity(com.github.goldy1992.mp3player.client.testsupport.activities.FolderActivityInjectorTestImpl::class.java, intent).setup()
    }

    @Test
    fun testOnBackPressed() {
        scenario!!.get().onBackPressed()
        Assert.assertTrue(scenario!!.get().isFinishing)
    }
}