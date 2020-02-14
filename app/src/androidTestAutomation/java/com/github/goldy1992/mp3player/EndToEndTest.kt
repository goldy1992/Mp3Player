package com.github.goldy1992.mp3player

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.view.accessibility.AccessibilityEvent
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.*
import com.github.goldy1992.mp3player.TestUtils.resourceId
import com.github.goldy1992.mp3player.client.RecyclerViewCountAssertion
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class EndToEndTest {

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
        assertSplashScreenActivity()
        waitForMainActivityToLoad()
    }


    @Test
    fun playSongTest() {
        val released : Boolean = false
val fastScrollHandleBubbleId : String = resourceId("fastscroll_bubble")
        val fastScrollHandleId : String = resourceId("fastscroll_handle")

        val recyclerViewId : String = resourceId("recyclerView")

        var recyclerViewInteraction : ViewInteraction = onView(withId(R.id.recyclerView))
        recyclerViewInteraction.check(RecyclerViewCountAssertion(37))
        recyclerViewInteraction.perform(longClick())

        // assert number of items in songs
    //    val recyclerView : UiObject2 = mDevice.findObject(By.res(recyclerViewId))

    //    val recyclerView1 : UiObject = mDevice.findObject(UiSelector().resourceId(recyclerViewId))

  //      val recyclerViewScrollable : UiScrollable = UiScrollable(UiSelector().resourceId(recyclerViewId))

//        assertTrue(recyclerViewScrollable.scrollToEnd(1))
//        val fastScrollHandle : UiObject2  = getUiObject2FromId(fastScrollHandleId)


        //mDevice.wait(Until.hasObject(By.res(fastScrollHandleBubbleId)), LAUNCH_TIMEOUT)
//        val fastScrollHandleBubble : UiObject2 = getUiObject2FromId(fastScrollHandleBubbleId)
//        assertEquals("0", fastScrollHandleBubble.text)
        //recyclerView1.


        // assert name of first

        // assert name of last
        assertTrue(true)

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


    private fun assertSplashScreenActivity() {
        val expectedTitle = "Music Player"

        val imageViewId : String = resourceId("imageView")
        mDevice.wait(
                Until.hasObject(By.res(imageViewId)),
                LAUNCH_TIMEOUT
        )

        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
        onView(withId(R.id.titleView)).check(matches(isDisplayed()))
        onView(withId(R.id.titleView)).check(matches(withText(containsString(expectedTitle))))
//        val logoUi : UiObject = mDevice.findObject(UiSelector().resourceId(imageViewId))
//        assertTrue(logoUi.exists())
//
//        val titleViewId : String = resourceId("titleView")
//        val title : UiObject = mDevice.findObject(UiSelector().resourceId(titleViewId))
//
//        assertTrue(title.exists())
//
//        assertEquals(expectedTitle, title.text)

    }

    private fun getUiObjectFromId(id : String) : UiObject {
        return mDevice.findObject(UiSelector().resourceId(id))
    }

    private fun getUiObject2FromId(id : String) : UiObject2 {
        return mDevice.findObject(By.res(id))
    }

}