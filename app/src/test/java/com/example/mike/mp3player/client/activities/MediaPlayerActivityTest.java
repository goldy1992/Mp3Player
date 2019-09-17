package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class MediaPlayerActivityTest {

    private static final String MOCK_MEDIA_ID = "MOCK_MEDIA_ID";
    /** Intent */
    private Intent intent;
    /** Activity controller */
    ActivityController<MediaPlayerActivityInjectorTestImpl> activityController;
    /**
     *
     */
    private MediaPlayerActivity mediaPlayerActivity;
    /**
     */
    private MediaSessionCompat mediaSessionCompat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        mediaSessionCompat = new MediaSessionCompat(context, "TAG");
        this.intent = new Intent(ApplicationProvider.getApplicationContext(), SearchResultActivity.class);
        this.activityController = Robolectric.buildActivity(MediaPlayerActivityInjectorTestImpl.class, intent).setup();
        this.mediaPlayerActivity = activityController.get();
    }

    @After
    public void tearDown() {
        activityController.stop().destroy();
    }
    /**
     * Asserts that all the items are created successfully
     */
    @Test
    public void testInitialisation()  {
        assertNotNull(mediaPlayerActivity.getMetadataTitleBarFragment());
        assertNotNull(mediaPlayerActivity.getPlaybackTrackerFragment());
        assertNotNull(mediaPlayerActivity.getPlayToolBarFragment());
        assertNotNull(mediaPlayerActivity.getMediaControlsFragment());
        assertNotNull(mediaPlayerActivity.getAlbumArtFragment());
    }

}