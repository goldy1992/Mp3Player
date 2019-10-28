package com.example.mike.mp3player;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.mike.mp3player.client.activities.MainActivityInjectorAndroidTestImpl;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.mike.mp3player.R.id.tabs;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
public class EndToEndTest {

    @Rule
    public ActivityTestRule<MainActivityInjectorAndroidTestImpl> mActivityTestRule = new ActivityTestRule<>(MainActivityInjectorAndroidTestImpl.class);


    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Before
    public void setup() {
        IdlingResource idlingResource = mActivityTestRule.getActivity();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void splashScreenEntryActivityTest() {
       // mActivityTestRule.getActivity().
       // mActivityTestRule.getScenario();
        ViewInteraction textView = onView(
                allOf(withText("Songs"),
                        childAtPosition(
                                allOf(withId(tabs),
                                        withParent(withId(R.id.rootItemsPager))),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("SONGS")));

        ViewInteraction textView2 = onView(
                allOf(withText("Folders"),
                        childAtPosition(
                                allOf(withId(tabs),
                                        withParent(withId(R.id.rootItemsPager))),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("FOLDERS")));

        ViewInteraction textView3 = onView(
                allOf(withText("Folders"),
                        childAtPosition(
                                allOf(withId(tabs),
                                        withParent(withId(R.id.rootItemsPager))),
                                2),
                        isDisplayed()));
        textView3.perform(click());
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
