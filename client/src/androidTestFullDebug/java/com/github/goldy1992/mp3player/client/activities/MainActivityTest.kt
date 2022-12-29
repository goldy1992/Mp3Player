package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.dagger.modules.MediaRepositoryModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UninstallModules(MediaRepositoryModule::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(*MainActivity.calculatePermissions())

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()


    lateinit var context : Context

    lateinit var scenario : ActivityScenario<MainActivity>

    private lateinit var mDevice: UiDevice


    @Before
    fun setUp() {
        this.context = getInstrumentation().context
        this.scenario = ActivityScenario.launch(MainActivity::class.java)
        this.mDevice = UiDevice.getInstance(getInstrumentation())
    }

    @Test
    fun testMain() {
       // assertSplashScreenActivity()
        waitForMainActivityToLoad(composeTestRule = composeTestRule)
    }

    private fun assertSplashScreenActivity() {
        val splashIconContentDescription = context.getString(R.string.splash_screen_icon)
        val appTitle = context.getString(R.string.app_title)
        mDevice.wait(
            Until.hasObject(By.descContains(splashIconContentDescription)),
            LAUNCH_TIMEOUT
        )
        runBlocking {
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription(splashIconContentDescription)
                .assertExists().assertIsDisplayed()
            composeTestRule.onNodeWithText(appTitle).assertExists().assertIsDisplayed()
        }
    }

private fun waitForMainActivityToLoad(composeTestRule: ComposeTestRule) {
    composeTestRule.onRoot(true).printToLog("tag")
    val library = context.getString(R.string.library)

    runBlocking {
        composeTestRule.awaitIdle()
           composeTestRule.waitUntil(LAUNCH_TIMEOUT) {
            try {
                val nodes = composeTestRule.onAllNodesWithText(library, substring = true, useUnmergedTree = true)
                nodes.assertCountEquals(2)

                true
            } catch (assertionError: AssertionError) {
                false
            }
        }
    }
}


    private companion object {
        const val LAUNCH_TIMEOUT = 25000L
    }
}