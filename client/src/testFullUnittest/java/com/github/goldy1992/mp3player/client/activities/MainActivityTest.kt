package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode

@HiltAndroidTest
@UninstallModules(MediaBrowserAdapterModule::class,
    MediaControllerAdapterModule::class)
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest {

    private lateinit var scenario : ActivityScenario<MainActivityUnitTestImpl>

    @Rule
    @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        rule.inject()
    }


    @Test
    fun testOnCreateViewIntent() {
        val intent = Intent(InstrumentationRegistry.getInstrumentation().context, MainActivityUnitTestImpl::class.java)
        intent.action = Intent.ACTION_VIEW
        val expectedUri = mock<Uri>()
        intent.data = expectedUri
        scenario = ActivityScenario.launch(intent)
        scenario.onActivity { activity: MainActivity ->
            activity.onPermissionGranted()
            Assert.assertEquals(expectedUri, activity.trackToPlay)
        }
    }
/* TODO: use these tests when intents to play a song from file is supported
    @Test
    fun testOnNewIntentWithoutViewAction() {
        launchActivityAndConnect()
        scenario.onFragment { activity: MediaPlayerFragment ->
            val newIntent = Intent(context, MediaPlayerFragment::class.java)
            val testUri = mock<Uri>()
            newIntent.data = testUri
            val spiedMediaControllerAdapter = spy(activity.mediaControllerAdapter)
            activity.mediaControllerAdapter = spiedMediaControllerAdapter
            //    activity.onNewIntent(newIntent)
            verify(spiedMediaControllerAdapter, never()).playFromUri(testUri, null)
        }
    }
    @Test
    fun testOnNewIntentWithViewAction() {
        launchActivityAndConnect()
        scenario.onFragment { activity: MediaPlayerFragment ->
            val newIntent = Intent(context, MediaPlayerFragment::class.java)
            val testUri = mock<Uri>()
            newIntent.data = testUri
            newIntent.action = Intent.ACTION_VIEW
            val spiedMediaControllerAdapter = spy(activity.mediaControllerAdapter)
            activity.mediaControllerAdapter = spiedMediaControllerAdapter
        //    activity.onNewIntent(newIntent)
            verify(spiedMediaControllerAdapter, times(1)).playFromUri(testUri, null)
        }
    }
    */



}


