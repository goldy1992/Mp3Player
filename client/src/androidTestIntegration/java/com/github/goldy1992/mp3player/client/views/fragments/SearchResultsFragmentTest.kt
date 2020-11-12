package com.github.goldy1992.mp3player.client.views.fragments

import android.support.v4.media.session.MediaSessionCompat
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.github.goldy1992.mp3player.client.util.launchFragmentInHiltContainer
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
           val scenario = launchFragmentInHiltContainer<SearchResultsFragment>(navHostController = navController)


            assertTrue(true)
        }

//    // TODO: fix tests
//    @Test
//    fun testOnSongItemSelected() {
//        scenario.onFragment { activity: SearchResultsFragment ->
//            val SearchResultsFragmentSpied = spy(activity)
//            val spiedMediaControllerAdapter = spy(SearchResultsFragmentSpied.mediaControllerAdapter)
//            SearchResultsFragmentSpied.mediaControllerAdapter = spiedMediaControllerAdapter
//            val libraryId = "libId"
//            val mediaItem = MediaItemBuilder("id")
//                    .setMediaItemType(MediaItemType.SONGS)
//                    .setLibraryId(libraryId)
//                    .build()
//            SearchResultsFragmentSpied.itemSelected(mediaItem)
//            verify(SearchResultsFragmentSpied, never()).startActivity(any())
//            verify(spiedMediaControllerAdapter, times(1)).playFromMediaId(libraryId, null)
//        }
//    }
//
//    @Test
//    fun testOnFolderItemSelected() {
//        scenario.onFragment { activity: SearchResultsFragment ->
//            val SearchResultsFragmentSpied = spy(activity)
//            val spiedMediaControllerAdapter = spy(SearchResultsFragmentSpied.mediaControllerAdapter)
//            SearchResultsFragmentSpied.mediaControllerAdapter = spiedMediaControllerAdapter
//            val libraryId = "libId"
//            val mediaItem = MediaItemBuilder("id")
//                    .setMediaItemType(MediaItemType.FOLDERS)
//                    .setLibraryId(libraryId)
//                    .build()
//            SearchResultsFragmentSpied.itemSelected(mediaItem)
//            verify(SearchResultsFragmentSpied, times(1)).startActivity(any())
//            verify(spiedMediaControllerAdapter, never()).playFromMediaId(libraryId, null)
//        }
//    }

    }
