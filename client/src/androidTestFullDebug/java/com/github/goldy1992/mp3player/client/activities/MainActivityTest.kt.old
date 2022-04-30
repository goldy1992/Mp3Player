package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Looper
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.UserPreferencesModule
import org.mockito.kotlin.mock
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.Shadows
//import org.robolectric.annotation.LooperMode

@HiltAndroidTest
@UninstallModules(
    MediaBrowserAdapterModule::class,
    MediaControllerAdapterModule::class,
    UserPreferencesModule::class)
class MainActivityTest {

    private lateinit var scenario : ActivityScenario<MainActivity>

    @Rule(order = 0)
    @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        rule.inject()
    }

    /**
     * Ensures the uri is set correctly when the [MainActivity] is called with an [Intent] of action
     * [Intent.ACTION_VIEW]
     */
    @Test
    fun testOnCreateViewIntent() {
        val intent = Intent(InstrumentationRegistry.getInstrumentation().context, MainActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        val expectedUri = Uri.parse("/root")
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