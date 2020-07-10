package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import java.io.File

@HiltAndroidTest
@UninstallModules(GlideModule::class,
    MediaBrowserAdapterModule::class,
    MediaControllerAdapterModule::class)
@RunWith(RobolectricTestRunner::class)
class FolderActivityTest {

    private lateinit var scenario: ActivityScenario<FolderActivity>

    @Rule @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        rule.inject()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, FolderActivity::class.java)
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