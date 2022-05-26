package com.github.goldy1992.mp3player

//import com.github.goldy1992.mp3player.client.activities.MainActivityUtils.clickOnItemWithText
//import com.github.goldy1992.mp3player.client.activities.MainActivityUtils.createAndRegisterIdlingResource
//import com.github.goldy1992.mp3player.client.activities.MainActivityUtils.scrollToRecyclerViewPosition
//import com.github.goldy1992.mp3player.client.activities.MainActivityUtils.togglePlayPauseButton
//import com.github.goldy1992.mp3player.client.activities.MainActivityUtils.unregisterIdlingResource
import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.*
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
//        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation())
//        startApp()

    }

    @Test
    fun test1() {
        assertTrue(true)
    }
    /**
     * Asserts the first item in the song list is expected and when clicked the status of the
     * [PlayPauseButton] changes.
     */
/*    @Test
    fun playSongTest() {
          val scenario : ActivityScenario<MainActivity> = ActivityScenario.launch(MainActivity::class.java)
        assertSplashScreenActivity()
        waitForMainActivityToLoad(composeTestRule)
        println("starting test")
        val songsListContentDescr = context.getString(R.string.songs_list)
        val node = composeTestRule.onNodeWithContentDescription(songsListContentDescr) //    val awaitingMediaControllerIdlingResource = createAndRegisterIdlingResource()
        assertNotPlaying(composeTestRule, context)

        val position = 0
        val expectedSong : Song = SONGS[position]
        val songDePrimera = node.onChildAt(position)
        songDePrimera.assert(hasText(expectedSong.title))

        runBlocking {
            songDePrimera.performClick()
            composeTestRule.awaitIdle()
        }
        mDevice.waitForIdle()
        assertIsPlaying(composeTestRule, context)
        clickPause(composeTestRule, context)
        mDevice.waitForIdle()
        assertNotPlaying(composeTestRule, context)
        runBlocking {
            composeTestRule.awaitIdle()
        }
        while (!mDevice.openNotification()) {
            mDevice.waitForIdle()
        }

        val expectedAppName = context.getString(R.string.app_name)
        mDevice.hasObject(By.text(expectedAppName))
        val selector : UiSelector = UiSelector()
                .resourceId("android:id/title")
                .text(expectedSong.title)
        mDevice.findObject(selector).waitForExists(5000L)

        playFromNotificationBar(mDevice)
        closeNotifications(getApplicationContext<Context>())

        waitForPlaying(composeTestRule, context)
        goToNowPlayingScreen(composeTestRule, context)


        Log.i("tag", "now playing loaded")
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.mainClock.advanceTimeBy(1000L)
        composeTestRule.onNodeWithText(expectedSong.title).assertExists()
        composeTestRule.onNodeWithText(expectedSong.artist).assertExists()
        composeTestRule.mainClock.autoAdvance = true

       // TODO: assert correct image is displayed

        mDevice.pressBack()

        Log.i("tag", "back pressed")
        waitForMainActivityToLoad(composeTestRule)
   //     assertIsPlaying()
   //     unregisterIdlingResource(awaitingMediaControllerIdlingResource)
    }

//    /**
//     * end to end testFullUnittest for the search functionality
//     */
//    @Test
//    fun testSearch() {
//    //    val awaitingMediaControllerIdlingResource = createAndRegisterIdlingResource()
//     //   onView(withId(R.id.fragmentContainer)).perform(RegisterIdlingResourceAction(awaitingMediaControllerIdlingResource))
//        openSearchFragment()
//        SearchFragmentUtils.performSearchQuery("prim")
//        assertTrue(true)
//    }
    /**
     *
     */
    private fun startApp() {
        // Start from the home screen
        //mDevice.pressHome();
        // Wait for launcher
        val launcherPackage: String = mDevice.launcherPackageName
//        assertThat(launcherPackage, notNullValue())
//        mDevice.wait(
//                Until.hasObject(By.pkg(launcherPackage).depth(0)),
//                LAUNCH_TIMEOUT
//        )
        // Launch the app
        val context = getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
                TEST_PACKAGE_NAME).apply {
            // Clear out any previous instances
            this?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)
    } */

    private fun waitForMainActivityToLoad(composeTestRule: ComposeTestRule) {
        val folders = context.getString(R.string.folders)
        composeTestRule.waitUntil(25000L) {
            try {
                composeTestRule.onNodeWithText(folders).assertExists()
                true
            } catch (assertionError: AssertionError) {
                false
            }
        }
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
    private fun goToNowPlayingScreen(composeTestRule: ComposeTestRule, context: Context) {
        val bottomAppBarDescr = context.getString(R.string.bottom_app_bar)
        composeTestRule.onNodeWithContentDescription(bottomAppBarDescr).performGesture {
            this.click(this.percentOffset(0.9f, 0.9f))
        }


            val nowPlayingScreen = context.getString(R.string.now_playing_screen)
            TestUtils.composeWaitUntilAssertionTrue(composeTestRule) {
                composeTestRule.onNodeWithContentDescription(nowPlayingScreen).assertExists()
            }

    }

    fun togglePlayPauseButton(composeTestRule: ComposeTestRule, context: Context) {
        val playButtonContentDescription = context.getString(R.string.play)
        val pauseButtonContentDescription = context.getString(R.string.pause)


        val playPauseButton : String = TestUtils.resourceId("playPauseButton")
        mDevice.findObject(UiSelector().resourceId(playPauseButton)).click()
    }

}