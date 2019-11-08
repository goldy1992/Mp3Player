package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class MediaPlayerActivityTest {

    private static final String MOCK_MEDIA_ID = "MOCK_MEDIA_ID";
    /** Intent */
    private Intent intent;
    /** Activity controller */
    ActivityController<MediaPlayerActivityInjectorTestImpl> activityController;
    /**
     */
    private MediaSessionCompat mediaSessionCompat;
    /**
     *
     */
    private Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.mediaSessionCompat = new MediaSessionCompat(context, "TAG");
        this.intent = new Intent(context, MediaPlayerActivity.class);
    }

    @After
    public void tearDown() {
        activityController.stop().destroy();
    }
    /**
     * Asserts that all the items are created successfully
     */
    @Test
    public void testInitialisationWithNormalIntent()  {
        createAndStartActivity();
        MediaPlayerActivity mediaPlayerActivity = activityController.get();
        assertNotNull(mediaPlayerActivity.getPlaybackTrackerFragment());
        assertNotNull(mediaPlayerActivity.getPlayToolBarFragment());
        assertNotNull(mediaPlayerActivity.getMediaControlsFragment());
    }

    private void createAndStartActivity() {
        this.activityController = Robolectric.buildActivity(MediaPlayerActivityInjectorTestImpl.class, intent).setup();
    }

    @Test
    public void testOnCreateViewIntent() {
        this.intent.setAction(Intent.ACTION_VIEW);
        Uri expectedUri = mock(Uri.class);
        this.intent.setData(expectedUri);
        createAndStartActivity();
        MediaPlayerActivity mediaPlayerActivity = activityController.get();
        assertEquals(expectedUri, mediaPlayerActivity.getTrackToPlay());
    }

}