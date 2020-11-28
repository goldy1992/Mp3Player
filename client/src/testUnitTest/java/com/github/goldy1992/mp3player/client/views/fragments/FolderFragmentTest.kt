package com.github.goldy1992.mp3player.client.views.fragments

import androidx.fragment.app.testing.FragmentScenario
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@HiltAndroidTest
@UninstallModules(GlideModule::class)
@RunWith(RobolectricTestRunner::class)
@Ignore
class FolderFragmentTest {

    private lateinit var scenario: FragmentScenario<FolderFragment>

    @Rule @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        rule.inject()
        val folder = File("/a/b/xyz")
        val mediaItem = MediaItemBuilder("id")
                .setLibraryId("xyz")
                .setDirectoryFile(folder)
                .build()
        scenario = FragmentScenario.launch(FolderFragment::class.java)
    }

    @Test
    fun firstTest() {
        assertTrue(true)
    }

/*
    @Test
    fun testOnBackPressed() {
        scenario.onFragment { activity : FolderFragment ->
//            activity.onBackPressed()
//            Assert.assertNotNull(activity.isFinishing)
        }
    } */
}