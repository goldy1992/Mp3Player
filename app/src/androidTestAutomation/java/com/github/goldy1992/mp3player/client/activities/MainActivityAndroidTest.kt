package com.github.goldy1992.mp3player.client.activities

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.TestUtils.assertTabName
import com.github.goldy1992.mp3player.TestUtils.withRecyclerView
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.google.android.material.tabs.TabLayout
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityAndroidTest {

    private lateinit var idlingResource : IdlingResource;


    @get:Rule
    val mActivityTestRule : ActivityTestRule<MainActivityInjectorAndroidTestImpl> = ActivityTestRule(MainActivityInjectorAndroidTestImpl::class.java)

    @get:Rule
    val  mGrantPermissionRule : GrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.WRITE_EXTERNAL_STORAGE")

    @Before
    fun setup() {
        this.idlingResource = mActivityTestRule.activity as IdlingResource
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }


    @Test
    fun firstTest() {

        val mainActivity = mActivityTestRule.activity
        var tabLayout : TabLayout = mainActivity.findViewById(R.id.tabLayout)

        val expectedTabCount = 2;
        val actualTabCount = tabLayout.getTabCount();
        assertEquals(expectedTabCount, actualTabCount);

        assertTabName(tabLayout, 0, MediaItemType.SONGS.title);
        assertTabName(tabLayout, 1, MediaItemType.FOLDERS.title);

        onView(allOf(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                withId(R.id.recycler_view)))
                .check(matches(isDisplayed()));

        onView(withRecyclerView(R.id.recycler_view)!!
                .atPositionOnView(0, R.id.title))
                .check(matches(withText("#Dprimera")))

                .perform(scrollToPosition<RecyclerView.ViewHolder>(14));


        onView(withRecyclerView(R.id.recycler_view)!!
                .atPositionOnView(14, R.id.title))
                .check(matches(withText("Yuya")));
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