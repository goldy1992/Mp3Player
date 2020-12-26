package com.github.goldy1992.mp3player.client.views.fragments

import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import android.widget.SearchView
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.BaseRobot
import com.github.goldy1992.mp3player.client.FragmentHiltScenario
import com.github.goldy1992.mp3player.client.TestUtils.typeSearchViewText
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.CoreMatchers.allOf
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

    lateinit var scenario : FragmentHiltScenario<SearchResultsFragment>


    @Before
    fun setup() {
        rule.inject()
        this.navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        runOnUiThread {navController.setGraph(R.navigation.nav_graph)
        navController.setCurrentDestination(R.id.search_results_fragment)}
        scenario = FragmentHiltScenario.launch3FragmentInHiltContainer<SearchResultsFragment>(navHostController = navController)
    }

    @After
    fun tearDown() {
    }


    @Test
    fun testOnSongItemSelected() {
        scenario.onFragment {
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

    @Test
    fun testNoResultsFound() {
        /* used as an extra wait mechanism as the SearchFragment is added to the HiltTestActivity
         dynamically. */
        BaseRobot().waitForView(withId(R.id.search))
        val viewAction = typeSearchViewText("guapa")
        onView(withId(R.id.search))
               .perform(viewAction)
        // TODO: set up test to return different values depending on the search query.
    }

}