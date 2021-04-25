package com.github.goldy1992.mp3player.client.views.fragments

import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.github.goldy1992.mp3player.client.BaseRobot
import com.github.goldy1992.mp3player.client.FragmentHiltScenario
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.TestUtils.typeSearchViewText
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.github.goldy1992.mp3player.client.utils.IdlingResources
import com.github.goldy1992.mp3player.client.utils.SearchResultFragmentAndroidTestImpl
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matcher
import org.junit.After
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

    lateinit var scenario : FragmentHiltScenario<SearchResultFragmentAndroidTestImpl>


    @Before
    fun setup() {
        rule.inject()
        IdlingRegistry.getInstance().register(IdlingResources.idlingResource)
        this.navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        runOnUiThread {navController.setGraph(R.navigation.nav_graph)
        navController.setCurrentDestination(R.id.search_results_fragment)}
        scenario = FragmentHiltScenario.launch3FragmentInHiltContainer<SearchResultFragmentAndroidTestImpl>(navHostController = navController)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResources.idlingResource)
    }

    @Test
    fun testOnSongItemSelected() {
        scenario.onFragment {
            this as SearchResultsFragment
           // this.mediaControllerAdapter = mock<MediaControllerAdapter>()
            val spiedMediaControllerAdapter = spy(this.mediaControllerAdapter)
            this.mediaControllerAdapter = spiedMediaControllerAdapter
            val libraryId = "libId"
            val mediaItem = MediaItemBuilder("id")
                    .setMediaItemType(MediaItemType.SONGS)
                    .setLibraryId(libraryId)
                    .build()
            this.itemSelected(mediaItem)
            verify(spiedMediaControllerAdapter, times(1)).playFromMediaId(libraryId, null)
        }
    }

    @Test
    fun testOnFolderItemSelected() {
        scenario.onFragment {
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
        }
    }


    /**
     * 2 stages to test:
     * 1) Type query into search bar and assert that the current query is updated.
     * 2) Given the current query from stage 1, when no results are found the query should be
     * displayed as part of the message.
     */
    @Test
    fun testSearchQuerySent() {
        val expectedQuery = "guapa"
        /* used as an extra wait mechanism as the SearchFragment is added to the HiltTestActivity
         dynamically. */
        BaseRobot().waitForView(withId(R.id.search))
        var spiedMediaBrowserAdapter: MediaBrowserAdapter? = null
        scenario.onFragment {
            this as SearchResultsFragment
            spiedMediaBrowserAdapter = spy<MediaBrowserAdapter>(mediaBrowserAdapter)
            this.mediaBrowserAdapter = spiedMediaBrowserAdapter!!
        }

        // Stage 1, type in the query!
        val viewAction = typeSearchViewText(expectedQuery)
        onView(withId(R.id.search))
                .perform(viewAction)

        verify(spiedMediaBrowserAdapter, times(1))!!.search(expectedQuery, null)


        // Stage 2, pass response of no results found.
        IdlingResources.idlingResource.increment()
        scenario.onFragment {
            (this as SearchResultsFragment)
                    .onSearchResult(emptyList())
        }

        val result : Matcher<View> = withText(containsString("No search results found for query"))
        onView(result).check(matches(withText(containsString(expectedQuery))))
    }
}