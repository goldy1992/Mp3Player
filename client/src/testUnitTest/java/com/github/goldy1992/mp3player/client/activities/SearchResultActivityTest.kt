package com.github.goldy1992.mp3player.client.activities

import android.app.SearchManager
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@HiltAndroidTest
@UninstallModules(GlideModule::class,
        MediaBrowserAdapterModule::class,
        MediaControllerAdapterModule::class)
@RunWith(RobolectricTestRunner::class)
class SearchResultActivityTest {
    /** Intent  */
    private lateinit var intent: Intent

    private lateinit var scenario : ActivityScenario<SearchResultActivity>
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
        intent = Intent(ApplicationProvider.getApplicationContext(), SearchResultActivity::class.java)
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun testOnSongItemSelected() {
        scenario.onActivity { activity: SearchResultActivity ->
            val searchResultActivitySpied = spy(activity)
            val spiedMediaControllerAdapter = spy(searchResultActivitySpied.mediaControllerAdapter)
            searchResultActivitySpied.mediaControllerAdapter = spiedMediaControllerAdapter
            val libraryId = "libId"
            val mediaItem = MediaItemBuilder("id")
                    .setMediaItemType(MediaItemType.SONGS)
                    .setLibraryId(libraryId)
                    .build()
            searchResultActivitySpied.itemSelected(mediaItem)
            verify(searchResultActivitySpied, never()).startActivity(any())
            verify(spiedMediaControllerAdapter, times(1)).playFromMediaId(libraryId, null)
        }
    }

    @Test
    fun testOnFolderItemSelected() {
        scenario.onActivity { activity: SearchResultActivity ->
            val searchResultActivitySpied = spy(activity)
            val spiedMediaControllerAdapter = spy(searchResultActivitySpied.mediaControllerAdapter)
            searchResultActivitySpied.mediaControllerAdapter = spiedMediaControllerAdapter
            val libraryId = "libId"
            val mediaItem = MediaItemBuilder("id")
                    .setMediaItemType(MediaItemType.FOLDERS)
                    .setLibraryId(libraryId)
                    .build()
            searchResultActivitySpied.itemSelected(mediaItem)
            verify(searchResultActivitySpied, times(1)).startActivity(any())
            verify(spiedMediaControllerAdapter, never()).playFromMediaId(libraryId, null)
        }
    }

    @Test
    fun testHandleNewIntent() {
        scenario.onActivity { activity: SearchResultActivity ->

            val mediaBrowserAdapterSpied = spy(activity.mediaBrowserAdapter)
            activity.mediaBrowserAdapter = mediaBrowserAdapterSpied
            val expectedQuery = "QUERY"
            val intent = Intent()
            intent.action = Intent.ACTION_SEARCH
            intent.putExtra(SearchManager.QUERY, expectedQuery)
            activity.onNewIntent(intent)
            verify(mediaBrowserAdapterSpied, times(1)).search(expectedQuery, null)
        }
    }
}