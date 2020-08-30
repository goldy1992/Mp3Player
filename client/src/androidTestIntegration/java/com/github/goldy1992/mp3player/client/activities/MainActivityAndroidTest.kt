package com.github.goldy1992.mp3player.client.activities

import android.support.v4.media.MediaBrowserCompat
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.rule.GrantPermissionRule
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.google.android.material.tabs.TabLayout
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@HiltAndroidTest
class MainActivityAndroidTest {

    @Module
    @InstallIn(ApplicationComponent::class)
    class TestModule {

        @Singleton
        @Provides
        fun bindAnalyticsService() : ComponentClassMapper {
            return ComponentClassMapper.Builder()
                    .service(ComponentClassMapper::class.java)
                    .build()
        }
    }



    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    private lateinit var idlingResource : IdlingResource;

    private lateinit var activityScenario : ActivityScenario<MainActivityIdlingResourceImpl>

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


    @Before
    fun setup() {
        hiltRule.inject()

        this.activityScenario = ActivityScenario.launch(MainActivityIdlingResourceImpl::class.java)
        this.activityScenario.onActivity {
            this.idlingResource = it as IdlingResource

        }
         IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    /**
     * WHEN: straight after onCreate has been called
     * THEN: the TabLayout should be visible with 0 tabs.
     */
    @Test
    fun testNoTabsExistAfterOnCreate() {
        activityScenario.onActivity {
            val tabLayout: TabLayout = it.findViewById(R.id.tabLayout)
            val expectedTabCount = 0;
            val actualTabCount = tabLayout.tabCount;
            assertEquals(expectedTabCount, actualTabCount);
        }
    }

    @Test
    fun loadRootItems() {
        val expectedTab1Title = "sdffs"
        val expectedTab2Title = "sasdsaddffs"
        val rootSongs = MediaItemBuilder().setTitle(expectedTab1Title).setRootItemType(MediaItemType.SONGS).build()
        val rootFolders = MediaItemBuilder().setTitle(expectedTab2Title).setRootItemType(MediaItemType.FOLDERS).build()
        val rootItems = ArrayList<MediaBrowserCompat.MediaItem>()
        rootItems.add(rootSongs)
        rootItems.add(rootFolders)
        activityScenario.onActivity {
//            runBlocking {
//                    it.onChildrenLoaded("root", rootItems)
//
//                    val tabLayout: TabLayout = it.findViewById(R.id.tabLayout)
//                    val expectedTabCount = 2
//                    val actualTabCount = tabLayout.tabCount
//                    assertEquals(expectedTabCount, actualTabCount)
//
//                    assertTabName(tabLayout, 0, expectedTab1Title)
//                    assertTabName(tabLayout, 1, expectedTab2Title)
//
//            }
//            onView(allOf(
//                    withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
//                    withId(R.id.recycler_view)))
//                    .check(matches(isDisplayed()));
//
//            onView(withRecyclerView(R.id.recycler_view)!!
//                    .atPositionOnView(0, R.id.title))
//                    .check(matches(withText("#Dprimera")))
//
//                    .perform(scrollToPosition<RecyclerView.ViewHolder>(14));
//
//
//            onView(withRecyclerView(R.id.recycler_view)!!
//                    .atPositionOnView(14, R.id.title))
//                    .check(matches(withText("Yuya")));
//
        }
    }

    private fun  childAtPosition(
             parentMatcher : Matcher<View>, position : Int) : Matcher<View> {

        return object : TypeSafeMatcher<View>() {

            override fun describeTo(description : Description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            override fun matchesSafely(view : View) :  Boolean {
                var parent : ViewParent = view.getParent();
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view.equals((parent as ViewGroup).getChildAt(position));
            }
        };
    }
}