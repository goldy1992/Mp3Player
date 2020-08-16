package com.github.goldy1992.mp3player.client.views.fragments

import android.app.SearchManager
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
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

@HiltAndroidTest
@UninstallModules(GlideModule::class,
        MediaBrowserAdapterModule::class,
        MediaControllerAdapterModule::class)
@RunWith(RobolectricTestRunner::class)
@Ignore
class SearchResultsFragmentTest {
    /** Intent  */
    private lateinit var intent: Intent

    private lateinit var scenario : FragmentScenario<SearchResultsFragment>
    /** Activity controller  */

    private lateinit var mediaSessionCompat: MediaSessionCompat

    @Rule
    @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        rule.inject()
        val context = InstrumentationRegistry.getInstrumentation().context
        mediaSessionCompat = MediaSessionCompat(context, "TAG")
//        intent = Intent(ApplicationProvider.getApplicationContext(), SearchResultsFragment::class.java)
        scenario = FragmentScenario.launch(SearchResultsFragment::class.java)
    }

    @Test
    fun firstTest() {
        assertTrue(true)
    }

    /* TODO: fix tests
    @Test
    fun testOnSongItemSelected() {
        scenario.onFragment { activity: SearchResultsFragment ->
            val SearchResultsFragmentSpied = spy(activity)
            val spiedMediaControllerAdapter = spy(SearchResultsFragmentSpied.mediaControllerAdapter)
            SearchResultsFragmentSpied.mediaControllerAdapter = spiedMediaControllerAdapter
            val libraryId = "libId"
            val mediaItem = MediaItemBuilder("id")
                    .setMediaItemType(MediaItemType.SONGS)
                    .setLibraryId(libraryId)
                    .build()
            SearchResultsFragmentSpied.itemSelected(mediaItem)
            verify(SearchResultsFragmentSpied, never()).startActivity(any())
            verify(spiedMediaControllerAdapter, times(1)).playFromMediaId(libraryId, null)
        }
    }

    @Test
    fun testOnFolderItemSelected() {
        scenario.onFragment { activity: SearchResultsFragment ->
            val SearchResultsFragmentSpied = spy(activity)
            val spiedMediaControllerAdapter = spy(SearchResultsFragmentSpied.mediaControllerAdapter)
            SearchResultsFragmentSpied.mediaControllerAdapter = spiedMediaControllerAdapter
            val libraryId = "libId"
            val mediaItem = MediaItemBuilder("id")
                    .setMediaItemType(MediaItemType.FOLDERS)
                    .setLibraryId(libraryId)
                    .build()
            SearchResultsFragmentSpied.itemSelected(mediaItem)
            verify(SearchResultsFragmentSpied, times(1)).startActivity(any())
            verify(spiedMediaControllerAdapter, never()).playFromMediaId(libraryId, null)
        }
    }


     */
}