package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import com.github.goldy1992.mp3player.client.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

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
        waitForMainActivityToLoad(composeTestRule = composeTestRule)
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