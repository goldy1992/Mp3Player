package com.github.goldy1992.mp3player.client.activities;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@Ignore
@RunWith(AndroidJUnit4.class)
public class SplashScreenEntryActivityAndroidTestImplTest {
//
//    @Rule
//    public ActivityTestRule<SplashScreenEntryActivityInjector> mActivityTestRule = new ActivityTestRule<>(SplashScreenEntryActivityInjector.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void splashScreenEntryActivityAndroidTestImplTest() {

//        onView(withId(R.id.tabs)).check(new ViewAssertion() {
//            @Override
//            public void check(View view, NoMatchingViewException noViewFoundException) {
//                if (view instanceof TabLayout) {
//                    TabLayout tabLayout = (TabLayout) view;
//                    TabLayout.Tab tab = tabLayout.getTabAt(0);
//                    assertTrue(tab.getText().equals("SONGS"));
//                }
//                else
//                {
//                    throw noViewFoundException;
//                }
//            }
//        });


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
