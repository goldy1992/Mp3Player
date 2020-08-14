package com.github.goldy1992.mp3player.client.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.nhaarman.mockitokotlin2.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

@HiltAndroidTest
@UninstallModules(GlideModule::class,
        MediaBrowserAdapterModule::class,
        MediaControllerAdapterModule::class)
@RunWith(RobolectricTestRunner::class)
class MediaPlayerFragmentTest {
    /** Intent  */
    private var intent: Intent? = null

    private lateinit var scenario: FragmentScenario<MediaPlayerFragment>


    @Rule
    @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)


    /**
     */
    private lateinit var mediaSessionCompat: MediaSessionCompat
    /**
     *
     */
    private lateinit var context: Context

    @Before
    fun setup() {
        rule.inject()
        context = InstrumentationRegistry.getInstrumentation().context
        mediaSessionCompat = MediaSessionCompat(context, "TAG")
//        intent = Intent(context, MediaPlayerFragment::class.java)
    }

    @Test
    fun testOnCreateViewIntent() {
        intent!!.action = Intent.ACTION_VIEW
        val expectedUri = mock<Uri>()
        intent!!.data = expectedUri
//        launchActivityAndConnect()
        scenario.onFragment { activity: MediaPlayerFragment ->
            Assert.assertEquals(expectedUri, activity.trackToPlay)
        }
    }

    @Test
    fun testOnNewIntentWithoutViewAction() {
//        launchActivityAndConnect()
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

//    @Test
//    fun testOnNewIntentWithViewAction() {
//        launchActivityAndConnect()
//        scenario.onFragment { activity: MediaPlayerFragment ->
//            val newIntent = Intent(context, MediaPlayerFragment::class.java)
//            val testUri = mock<Uri>()
//            newIntent.data = testUri
//            newIntent.action = Intent.ACTION_VIEW
//            val spiedMediaControllerAdapter = spy(activity.mediaControllerAdapter)
//            activity.mediaControllerAdapter = spiedMediaControllerAdapter
//        //    activity.onNewIntent(newIntent)
//            verify(spiedMediaControllerAdapter, times(1)).playFromUri(testUri, null)
//        }
//    }

//    private fun launchActivityAndConnect() {
//        scenario = ActivityScenario.launch(intent)
//        scenario.onFragment { activity -> activity.onConnected() }
//    }
}