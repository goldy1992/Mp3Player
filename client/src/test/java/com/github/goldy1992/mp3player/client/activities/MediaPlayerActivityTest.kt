package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaPlayerActivityTest {
    /** Intent  */
    private var intent: Intent? = null

    private lateinit var scenario: ActivityScenario<MediaPlayerActivityInjectorTestImpl>

    /**
     */
    private lateinit var mediaSessionCompat: MediaSessionCompat
    /**
     *
     */
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        mediaSessionCompat = MediaSessionCompat(context, "TAG")
        intent = Intent(context, MediaPlayerActivityInjectorTestImpl::class.java)
    }

    @Test
    fun testOnCreateViewIntent() {
        intent!!.action = Intent.ACTION_VIEW
        val expectedUri = mock<Uri>()
        intent!!.data = expectedUri
        scenario = ActivityScenario.launch(intent)
        scenario.onActivity { activity: MediaPlayerActivityInjectorTestImpl ->
            Assert.assertEquals(expectedUri, activity.trackToPlay)
        }
    }

    @Test
    fun testOnNewIntentWithoutViewAction() {
        scenario = ActivityScenario.launch(intent)
        scenario.onActivity { activity: MediaPlayerActivityInjectorTestImpl ->
            val newIntent = Intent(context, MediaPlayerActivity::class.java)
            val testUri = mock<Uri>()
            newIntent.data = testUri
            val spiedMediaControllerAdapter = spy(activity.mediaControllerAdapter)
            activity.mediaControllerAdapter = spiedMediaControllerAdapter
            activity.onNewIntent(newIntent)
            verify(spiedMediaControllerAdapter, never()).playFromUri(testUri, null)
        }
    }

    @Test
    fun testOnNewIntentWithViewAction() {
        scenario = ActivityScenario.launch(intent)
        scenario.onActivity { activity: MediaPlayerActivityInjectorTestImpl ->
            val newIntent = Intent(context, MediaPlayerActivity::class.java)
            val testUri = mock<Uri>()
            newIntent.data = testUri
            newIntent.action = Intent.ACTION_VIEW
            val spiedMediaControllerAdapter = spy(activity.mediaControllerAdapter)
            activity.mediaControllerAdapter = spiedMediaControllerAdapter
            activity.onNewIntent(newIntent)
            verify(spiedMediaControllerAdapter, times(1)).playFromUri(testUri, null)
        }
    }

    @Test
    fun onMetadataChanged() {
        scenario = ActivityScenario.launch(intent)
        scenario.onActivity { activity: MediaPlayerActivityInjectorTestImpl ->
            val spiedMediaControllerAdapter = spy(activity.mediaControllerAdapter)
            activity.mediaControllerAdapter = spiedMediaControllerAdapter
            val mediaMetadataCompat = MediaMetadataCompat.Builder().build()
            activity.onMetadataChanged(mediaMetadataCompat)
            verify(spiedMediaControllerAdapter, times(1)).currentQueuePosition
        }
    }
}