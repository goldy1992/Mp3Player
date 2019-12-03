package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.android.synthetic.main.activity_media_player.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class MediaPlayerActivityTest {
    /** Intent  */
    private var intent: Intent? = null
    /** Activity controller  */
    var activityController: ActivityController<MediaPlayerActivityInjectorTestImpl>? = null
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
        intent = Intent(context, MediaPlayerActivity::class.java)
    }

    @After
    fun tearDown() {
        activityController!!.stop().destroy()
    }

    /**
     * Asserts that all the items are created successfully
     */
    @Test
    fun testInitialisationWithNormalIntent() {
        createAndStartActivity()
        val mediaPlayerActivity: MediaPlayerActivity = activityController!!.get()
        Assert.assertNotNull(mediaPlayerActivity.playbackTrackerFragment)
        Assert.assertNotNull(mediaPlayerActivity.playToolbarFragment)
        Assert.assertNotNull(mediaPlayerActivity.mediaControlsFragment)
    }

    private fun createAndStartActivity() {
        activityController = Robolectric.buildActivity(MediaPlayerActivityInjectorTestImpl::class.java, intent).setup()
    }

    @Test
    fun testOnCreateViewIntent() {
        intent!!.action = Intent.ACTION_VIEW
        val expectedUri = Mockito.mock(Uri::class.java)
        intent!!.data = expectedUri
        createAndStartActivity()
        val mediaPlayerActivity: MediaPlayerActivity = activityController!!.get()
        Assert.assertEquals(expectedUri, mediaPlayerActivity.trackToPlay)
    }

    @Test
    fun testOnNewIntentWithoutViewAction() {
        createAndStartActivity()
        val newIntent = Intent(context, MediaPlayerActivity::class.java)
        val testUri = Mockito.mock(Uri::class.java)
        newIntent.data = testUri
        val mediaPlayerActivity: MediaPlayerActivity = activityController!!.get()
        val spiedMediaControllerAdapter = Mockito.spy(mediaPlayerActivity.mediaControllerAdapter)
        mediaPlayerActivity.mediaControllerAdapter = spiedMediaControllerAdapter
        mediaPlayerActivity.onNewIntent(newIntent)
        Mockito.verify(spiedMediaControllerAdapter, Mockito.never())!!.playFromUri(testUri, null)
    }

    @Test
    fun testOnNewIntentWithViewAction() {
        createAndStartActivity()
        val newIntent = Intent(context, MediaPlayerActivity::class.java)
        val testUri = Mockito.mock(Uri::class.java)
        newIntent.data = testUri
        newIntent.action = Intent.ACTION_VIEW
        val mediaPlayerActivity: MediaPlayerActivity = activityController!!.get()
        val spiedMediaControllerAdapter = Mockito.spy(mediaPlayerActivity.mediaControllerAdapter)
        mediaPlayerActivity.mediaControllerAdapter = spiedMediaControllerAdapter
        mediaPlayerActivity.onNewIntent(newIntent)
        Mockito.verify(spiedMediaControllerAdapter, Mockito.times(1))!!.playFromUri(testUri, null)
    }

    @Test
    fun onMetadataChanged() {
        createAndStartActivity()
        val mediaPlayerActivity: MediaPlayerActivity = activityController!!.get()
        val spiedMediaControllerAdapter = Mockito.spy(mediaPlayerActivity.mediaControllerAdapter)
        mediaPlayerActivity.mediaControllerAdapter = spiedMediaControllerAdapter
        val mediaMetadataCompat = MediaMetadataCompat.Builder().build()
        mediaPlayerActivity.onMetadataChanged(mediaMetadataCompat)
        Mockito.verify(spiedMediaControllerAdapter, Mockito.times(1))!!.currentQueuePosition
    }

    companion object {
        private const val MOCK_MEDIA_ID = "MOCK_MEDIA_ID"
    }
}