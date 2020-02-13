package com.github.goldy1992.mp3player

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.*
import com.github.goldy1992.mp3player.TestUtils.resourceId
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
        assertSplashScreenActity()
        waitForMainActivityToLoad()
    }


    @Test
    fun firstTest() {

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


    private fun assertSplashScreenActity() {
        val imageViewId : String = resourceId("imageView")
        mDevice.wait(
                Until.hasObject(By.res("com.github.goldy1992.mp3player.automation:id/imageView")),
                LAUNCH_TIMEOUT
        )

        val logoUi : UiObject = mDevice.findObject(UiSelector().resourceId(imageViewId))
        assertTrue(logoUi.exists())

        val titleViewId : String = resourceId("titleView")
        val title : UiObject = mDevice.findObject(UiSelector().resourceId(titleViewId))
        assertTrue(title.exists())
        val expectedTitle = "Music Player"
        assertEquals(expectedTitle, title.text)

    }
}