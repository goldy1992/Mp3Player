package com.github.goldy1992.mp3player.client.views.fragments

import android.support.v4.media.session.MediaSessionCompat
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.github.goldy1992.mp3player.client.launchFragmentInHiltContainer
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(
    MediaBrowserAdapterModule::class,
    MediaControllerAdapterModule::class)
class SearchResultsFragmentTest {

    /** Activity controller  */
    private lateinit var mediaSessionCompat: MediaSessionCompat

    private lateinit var navController : TestNavHostController

    @Rule
    @JvmField
    val rule: HiltAndroidRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        rule.inject()
        val context = InstrumentationRegistry.getInstrumentation().context
//        mediaSessionCompat = MediaSessionCompat(context, "TAG")
        // Create a TestNavHostController
        this.navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        runOnUiThread {navController.setGraph(R.navigation.nav_graph)
        navController.setCurrentDestination(R.id.search_results_fragment)}
    }

        @Test
        fun firstTest() {
           val scenario = launchFragmentInHiltContainer<SearchResultsFragment>(navHostController = navController, action = {


           })
            assertTrue(true)
        }

//    // TODO: fix tests
    @Test
    fun testOnSongItemSelected() {
        launchFragmentInHiltContainer<SearchResultsFragment>(navHostController = navController, action = {
            this as SearchResultsFragment
            val spiedMediaControllerAdapter = spy(this.mediaControllerAdapter)
            this.mediaControllerAdapter = spiedMediaControllerAdapter
            val libraryId = "libId"
            val mediaItem = MediaItemBuilder("id")
                    .setMediaItemType(MediaItemType.SONGS)
                    .setLibraryId(libraryId)
                    .build()
            this.itemSelected(mediaItem)
            verify(spiedMediaControllerAdapter, times(1)).playFromMediaId(libraryId, null)
        })
    }

    @Test
    fun testOnFolderItemSelected() {
        launchFragmentInHiltContainer<SearchResultsFragment>(navHostController = navController, action = {
            this as SearchResultsFragment
              val spiedMediaControllerAdapter = spy(mediaControllerAdapter)
            mediaControllerAdapter = spiedMediaControllerAdapter
            val libraryId = "libId"
            val mediaItem = MediaItemBuilder("id")
                    .setMediaItemType(MediaItemType.FOLDERS)
                    .setLibraryId(libraryId)
                    .build()
            this.itemSelected(mediaItem)
               verify(spiedMediaControllerAdapter, never()).playFromMediaId(libraryId, null)
        })
    }

}
