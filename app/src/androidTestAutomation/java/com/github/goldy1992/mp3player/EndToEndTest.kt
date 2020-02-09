package com.github.goldy1992.mp3player

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.SearchCondition
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.github.goldy1992.mp3player.client.activities.MainActivityInjectorAndroidTestImpl
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
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

    private val BASIC_SAMPLE_PACKAGE = "com.github.goldy1992.mp3player.automation"

    private val LAUNCH_TIMEOUT = 25000L

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private  lateinit var  mDevice: UiDevice

    @Before
    fun setup() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation())
        startApp()
        waitForMainActivityToLoad()
    }


    @Test
    fun firstTest() {

    }

    private fun startApp() {
        // Start from the home screen
        mDevice.pressHome();
        // Wait for launcher
        val launcherPackage: String = mDevice.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        mDevice.wait(
                Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT
        )
        // Launch the app
        val context = getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
                BASIC_SAMPLE_PACKAGE).apply {
            // Clear out any previous instances
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)
    }

    private fun waitForMainActivityToLoad() {
        mDevice.wait(Until.hasObject(By.text("Folders")), LAUNCH_TIMEOUT)
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


    private fun allowPermissionsIfNeeded() {

        val accept : String = "PERMITIR"
        // Wait for the app to appear
        mDevice.wait(Until.findObject(By.text(accept)), 2000L)

        val allowPermissions = mDevice.findObject(UiSelector().text(accept))
        if (allowPermissions.exists()) {
            try {
                allowPermissions.click()
            } catch (e: UiObjectNotFoundException) {
                Log.e("error", "There is no permissions dialog to interact with ")
            }
        }

    }
}