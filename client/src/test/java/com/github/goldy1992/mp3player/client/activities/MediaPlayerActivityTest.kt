package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivityInjectorTestImpl
import kotlinx.android.synthetic.main.activity_media_player.*

@RunWith(RobolectricTestRunner::class)
class MediaPlayerActivityTest {
    /** Intent  */
    private var intent: Intent? = null

    private lateinit var scenario: ActivityScenario<MediaPlayerActivityInjectorTestImpl>

    /**
     */
    private var mediaSessionCompat: MediaSessionCompat? = null
    /**
     *
     */
    private var context: Context? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        context = InstrumentationRegistry.getInstrumentation().context
        mediaSessionCompat = MediaSessionCompat(context, "TAG")
        intent = Intent(context, MediaPlayerActivityInjectorTestImpl::class.java)
    }

    @Test
    fun testOnCreateViewIntent() {
        intent!!.action = Intent.ACTION_VIEW
        val expectedUri = Mockito.mock(Uri::class.java)
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
            val testUri = Mockito.mock(Uri::class.java)
            newIntent.data = testUri
            val spiedMediaControllerAdapter = Mockito.spy(activity.mediaControllerAdapter)
            activity.mediaControllerAdapter = spiedMediaControllerAdapter
            activity.onNewIntent(newIntent)
            Mockito.verify(spiedMediaControllerAdapter, Mockito.never())!!.playFromUri(testUri, null)
        }
    }

    @Test
    fun testOnNewIntentWithViewAction() {
        scenario = ActivityScenario.launch(intent)
        scenario.onActivity { activity: MediaPlayerActivityInjectorTestImpl ->
            val newIntent = Intent(context, MediaPlayerActivity::class.java)
            val testUri = Mockito.mock(Uri::class.java)
            newIntent.data = testUri
            newIntent.action = Intent.ACTION_VIEW
            val spiedMediaControllerAdapter = Mockito.spy(activity.mediaControllerAdapter)
            activity.mediaControllerAdapter = spiedMediaControllerAdapter
            activity.onNewIntent(newIntent)
            Mockito.verify(spiedMediaControllerAdapter, Mockito.times(1))!!.playFromUri(testUri, null)
        }
    }

    @Test
    fun onMetadataChanged() {
        scenario = ActivityScenario.launch(intent)
        scenario.onActivity { activity: MediaPlayerActivityInjectorTestImpl ->
            val spiedMediaControllerAdapter = Mockito.spy(activity.mediaControllerAdapter)
            activity.mediaControllerAdapter = spiedMediaControllerAdapter
            val mediaMetadataCompat = MediaMetadataCompat.Builder().build()
            activity.onMetadataChanged(mediaMetadataCompat)
            Mockito.verify(spiedMediaControllerAdapter, Mockito.times(1))!!.currentQueuePosition
        }
    }
}