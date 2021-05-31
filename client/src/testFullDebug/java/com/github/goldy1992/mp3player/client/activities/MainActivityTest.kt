package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.nhaarman.mockitokotlin2.mock
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode

@HiltAndroidTest
@UninstallModules(
    MediaBrowserAdapterModule::class,
    MediaControllerAdapterModule::class)
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest {

    private lateinit var scenario : ActivityScenario<MainActivity>

    @Rule
    @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        rule.inject()
        scenario = ActivityScenario.launch(MainActivity::class.java)
//        scenario.onActivity { activity -> activity.onConnected() }
    }

    @Test
    fun testOnCreateViewIntent() {
        val intent = Intent(InstrumentationRegistry.getInstrumentation().context, MainActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        val expectedUri = mock<Uri>()
        intent.data = expectedUri
        launchActivityAndConnect(intent)
        scenario.onActivity { activity: MainActivity ->
            Assert.assertEquals(expectedUri, activity.trackToPlay)
        }
    }



    private fun launchActivityAndConnect(intent : Intent) {
        scenario = ActivityScenario.launch(intent)
    }


}