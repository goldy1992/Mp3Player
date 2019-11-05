package com.example.mike.mp3player;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.mike.mp3player.client.activities.MainActivity;
import com.example.mike.mp3player.client.activities.MainActivityInjectorAndroidTestImpl;
import com.example.mike.mp3player.commons.MediaItemType;
import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.mike.mp3player.TestUtils.assertTabName;
import static com.example.mike.mp3player.TestUtils.withRecyclerView;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;

@LargeTest
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
public class EndToEndTest {

    IdlingResource idlingResource;
    @Rule
    public ActivityTestRule<MainActivityInjectorAndroidTestImpl> mActivityTestRule = new ActivityTestRule<>(MainActivityInjectorAndroidTestImpl.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Before
    public void setup() {
        this.idlingResource = mActivityTestRule.getActivity();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void splashScreenEntryActivityTest() {
        Espresso.onIdle();
        MainActivity mainActivity = mActivityTestRule.getActivity();
        TabLayout tabLayout = mainActivity.findViewById(R.id.tabs);

        final int expectedTabCount = 2;
        final int actualTabCount = tabLayout.getTabCount();
        assertEquals(expectedTabCount, actualTabCount);

        assertTabName(tabLayout, 0, MediaItemType.SONGS.getTitle());
        assertTabName(tabLayout, 1, MediaItemType.FOLDERS.getTitle());

        onView(allOf(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                withId(R.id.recycler_view)))
                .check(matches(isDisplayed()));

        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.title))
                .check(matches(withText("#Dprimera")))

                .perform(scrollToPosition(14));


        onView(withRecyclerView(R.id.recycler_view)
                .atPositionOnView(14, R.id.title))
                .check(matches(withText("Yuya")));





    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
