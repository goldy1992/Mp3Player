package com.github.goldy1992.mp3player.client.activities

import android.app.SearchManager
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchResultActivityTest {
    /** Intent  */
    private var intent: Intent? = null

    private lateinit var scenario : ActivityScenario<SearchResultActivityInjectorTestImpl>
    /** Activity controller  */

    private var mediaSessionCompat: MediaSessionCompat? = null
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().context
        mediaSessionCompat = MediaSessionCompat(context, "TAG")
        intent = Intent(ApplicationProvider.getApplicationContext(), SearchResultActivityInjectorTestImpl::class.java)
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun testOnSongItemSelected() {
        scenario.onActivity { activity: SearchResultActivityInjectorTestImpl ->
            val searchResultActivitySpied = Mockito.spy(activity)
            val spiedMediaControllerAdapter = Mockito.spy(searchResultActivitySpied!!.mediaControllerAdapter)
            searchResultActivitySpied.mediaControllerAdapter = spiedMediaControllerAdapter
            val libraryId = "libId"
            val mediaItem = MediaItemBuilder("id")
                    .setMediaItemType(MediaItemType.SONGS)
                    .setLibraryId(libraryId)
                    .build()
            searchResultActivitySpied.itemSelected(mediaItem)
            Mockito.verify(searchResultActivitySpied, Mockito.never())!!.startActivity(ArgumentMatchers.any())
            Mockito.verify(spiedMediaControllerAdapter, Mockito.times(1))!!.playFromMediaId(libraryId, null)
        }
    }

    @Test
    fun testOnFolderItemSelected() {
        scenario.onActivity { activity: SearchResultActivityInjectorTestImpl ->
            val searchResultActivitySpied = Mockito.spy(activity)
            val spiedMediaControllerAdapter = Mockito.spy(searchResultActivitySpied!!.mediaControllerAdapter)
            searchResultActivitySpied.mediaControllerAdapter = spiedMediaControllerAdapter
            val libraryId = "libId"
            val mediaItem = MediaItemBuilder("id")
                    .setMediaItemType(MediaItemType.FOLDERS)
                    .setLibraryId(libraryId)
                    .build()
            searchResultActivitySpied.itemSelected(mediaItem)
            Mockito.verify(searchResultActivitySpied, Mockito.times(1))!!.startActivity(ArgumentMatchers.any())
            Mockito.verify(spiedMediaControllerAdapter, Mockito.never())!!.playFromMediaId(libraryId, null)
        }
    }

    @Test
    fun testHandleNewIntent() {
        scenario.onActivity { activity: SearchResultActivityInjectorTestImpl ->

            val mediaBrowserAdapterSpied = Mockito.spy(activity.mediaBrowserAdapter)
            activity.mediaBrowserAdapter = mediaBrowserAdapterSpied
            val expectedQuery = "QUERY"
            val intent = Intent()
            intent.action = Intent.ACTION_SEARCH
            intent.putExtra(SearchManager.QUERY, expectedQuery)
            activity.onNewIntent(intent)
            Mockito.verify(mediaBrowserAdapterSpied, Mockito.times(1))!!.search(expectedQuery, null)
        }
    }
}