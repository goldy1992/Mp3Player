package com.github.goldy1992.mp3player

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(androidx.test.runner.AndroidJUnit4::class)
class EndToEndTest {

    //    IdlingResource idlingResource;
//    @Rule
//    public ActivityTestRule<MainActivityInjectorAndroidTestImpl> mActivityTestRule = new ActivityTestRule<>(MainActivityInjectorAndroidTestImpl.class);
//
//    @Rule
//    public GrantPermissionRule mGrantPermissionRule =
//            GrantPermissionRule.grant(
//                    "android.permission.WRITE_EXTERNAL_STORAGE");
//
//    @Before
//    public void setup() {
//        this.idlingResource = mActivityTestRule.getActivity();
//        IdlingRegistry.getInstance().register(idlingResource);
//    }
//
//    @After
//    public void tearDown() {
//        IdlingRegistry.getInstance().unregister(idlingResource);
//    }
//
//    @Test
//    public void splashScreenEntryActivityTest() {
////        Espresso.onIdle();
////        MainActivity mainActivity = mActivityTestRule.getActivity();
////        TabLayout tabLayout = mainActivity.findViewById(R.id.tabs);
////
////        final int expectedTabCount = 2;
////        final int actualTabCount = tabLayout.getTabCount();
////        assertEquals(expectedTabCount, actualTabCount);
////
////        assertTabName(tabLayout, 0, MediaItemType.SONGS.getTitle());
////        assertTabName(tabLayout, 1, MediaItemType.FOLDERS.getTitle());
////
////        onView(allOf(
////                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
////                withId(R.id.recycler_view)))
////                .check(matches(isDisplayed()));
////
////        onView(withRecyclerView(R.id.recycler_view)
////                .atPositionOnView(0, R.id.title))
////                .check(matches(withText("#Dprimera")))
////
////                .perform(scrollToPosition(14));
////
////
////        onView(withRecyclerView(R.id.recycler_view)
////                .atPositionOnView(14, R.id.title))
////                .check(matches(withText("Yuya")));
////
//
//
//
//
//    }
//
//    private static Matcher<View> childAtPosition(
//            final Matcher<View> parentMatcher, final int position) {
//
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Child at position " + position + " in parent ");
//                parentMatcher.describeTo(description);
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                ViewParent parent = view.getParent();
//                return parent instanceof ViewGroup && parentMatcher.matches(parent)
//                        && view.equals(((ViewGroup) parent).getChildAt(position));
//            }
//        };
//    }

    private val LAUNCH_TIMEOUT = 5000L

    private  lateinit var  mDevice: UiDevice



    @Before
    fun setup() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation())
    }



    @Test
    fun firstTest() {

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        val launcherPackage : String? = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

    }

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private fun getLauncherPackageName(): String? { // Create launcher Intent
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        // Use PackageManager to get the launcher package name
        val pm = getApplicationContext<Context>().packageManager
        val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo.activityInfo.packageName
    }
}