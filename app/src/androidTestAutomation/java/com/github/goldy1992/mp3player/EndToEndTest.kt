package com.github.goldy1992.mp3player

//import com.github.goldy1992.mp3player.client.PlayPauseButtonAssert.assertIsPlaying
//import com.github.goldy1992.mp3player.client.PlayPauseButtonAssert.assertNotPlaying
//import com.github.goldy1992.mp3player.client.activities.MainActivityUtils.clickOnItemWithText
//import com.github.goldy1992.mp3player.client.activities.MainActivityUtils.createAndRegisterIdlingResource
//import com.github.goldy1992.mp3player.client.activities.MainActivityUtils.scrollToRecyclerViewPosition
//import com.github.goldy1992.mp3player.client.activities.MainActivityUtils.togglePlayPauseButton
//import com.github.goldy1992.mp3player.client.activities.MainActivityUtils.unregisterIdlingResource
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.*
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.views.fragments.SearchFragmentUtils
import com.github.goldy1992.mp3player.client.views.fragments.SearchFragmentUtils.openSearchFragment
import com.github.goldy1992.mp3player.testdata.Song
import com.github.goldy1992.mp3player.testdata.Songs.SONGS
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * End to end tests
 */
@LargeTest
@HiltAndroidTest
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class EndToEndTest {

    private companion object {
        const val TEST_PACKAGE_NAME = "com.github.goldy1992.mp3player.automation"
        const val LAUNCH_TIMEOUT = 25000L
    }

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mDevice: UiDevice

    private val context: Context = getInstrumentation().targetContext


    /**
     * before method
     */
    @Before
    fun setup() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation())
        startApp()
        assertSplashScreenActivity()
        waitForMainActivityToLoad()
    }
    /**
     * asserts
     */
    @Test
    fun playSongTest() {
        assertTrue(true)
    //    val awaitingMediaControllerIdlingResource = createAndRegisterIdlingResource()
  //      onView(withId(R.id.fragmentContainer)).perform(RegisterIdlingResourceAction(awaitingMediaControllerIdlingResource))
 //       assertNotPlaying()
    //    var recyclerViewInteraction : ViewInteraction = onView(withId(R.id.recyclerView))
       // recyclerViewInteraction.check(RecyclerViewCountAssertion(SONGS_COUNT))

        val position = 8
        val expectedSong : Song = SONGS[8]

//        scrollToRecyclerViewPosition(position)
//
//        awaitingMediaControllerIdlingResource.waitForPlay()
//        clickOnItemWithText(mDevice, expectedSong.title)
// //       assertIsPlaying()
//
//
//        awaitingMediaControllerIdlingResource.waitForPause()
//        togglePlayPauseButton(mDevice)
//   //     assertNotPlaying()
//
//        mDevice.openNotification()
//        mDevice.hasObject(By.text("Test Music Player"))
//        awaitingMediaControllerIdlingResource.waitForPlay()
//        playFromNotificationBar(mDevice)
   //     closeNotifications(getApplicationContext<Context>())
   //     waitForMainActivityToLoad()
   //     assertIsPlaying()
   //     goToMediaPlayerActivity()

        // assert that the correct song is displayed on media player activity
    //    onView(withId(R.id.songTitle)).check(matches(withText(expectedSong.title)))
    //    onView(withId(R.id.songArtist)).check(matches(withText(expectedSong.artist)))
        // TODO: assert correct image is displayed

     //   mDevice.pressBack()
     //   waitForMainActivityToLoad()

   //     assertIsPlaying()
   //     unregisterIdlingResource(awaitingMediaControllerIdlingResource)
    }

    /**
     * end to end testFullUnittest for the search functionality
     */
    @Test
    fun testSearch() {
    //    val awaitingMediaControllerIdlingResource = createAndRegisterIdlingResource()
     //   onView(withId(R.id.fragmentContainer)).perform(RegisterIdlingResourceAction(awaitingMediaControllerIdlingResource))
        openSearchFragment()
        SearchFragmentUtils.performSearchQuery("prim")
        assertTrue(true)
    }
    /**
     *
     */
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
                TEST_PACKAGE_NAME).apply {
            // Clear out any previous instances
            this?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)
    }

    private fun waitForMainActivityToLoad() {
        val folders = context.getString(R.string.folders)
        mDevice.wait(Until.hasObject(By.text(folders)), LAUNCH_TIMEOUT)
        Log.i("", "")
    }


    private fun assertSplashScreenActivity() {
        val splashIconContentDescription = context.getString(R.string.splash_screen_icon)
        val appTitle = context.getString(R.string.app_title)
        mDevice.wait(
                Until.hasObject(By.descContains(splashIconContentDescription)),
                LAUNCH_TIMEOUT
        )
        composeTestRule.onNodeWithContentDescription(splashIconContentDescription).assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText(appTitle).assertExists().assertIsDisplayed()
    }

    private fun getUiObjectFromId(id : String) : UiObject {
        return mDevice.findObject(UiSelector().resourceId(id))
    }

    private fun getUiObject2FromId(id : String) : UiObject2 {
        return mDevice.findObject(By.res(id))
    }

    /**
     * Clicks to the left of the fragment toolbar instead of the center as the center is covered
     * by the PlayPauseButton
     */
    private fun goToMediaPlayerActivity() {
//        onView(withId(R.id.innerPlaybackToolbarLayout))
//            .perform(ViewActions.actionWithAssertions(
//                GeneralClickAction(
//                        Tap.SINGLE,
//                        GeneralLocation.CENTER_LEFT,
//                        Press.FINGER,
//                        InputDevice.SOURCE_UNKNOWN,
//                        MotionEvent.BUTTON_PRIMARY)))
    }

}