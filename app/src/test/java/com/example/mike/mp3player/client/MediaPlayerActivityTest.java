package com.example.mike.mp3player.client;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.commons.Constants;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class MediaPlayerActivityTest {

    private static final String MOCK_MEDIA_ID = "MOCK_MEDIA_ID";
    /**
     * Intent
     */
    private Intent intent;

    /**
     *
     */
    private MediaSessionCompat mediaSessionCompat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        mediaSessionCompat = new MediaSessionCompat(context, "TAG");
        this.intent = new Intent(ApplicationProvider.getApplicationContext(), MediaPlayerActivity.class);
        this.intent.putExtra(Constants.MEDIA_SESSION, mediaSessionCompat.getSessionToken());
        this.intent.putExtra(Constants.MEDIA_ID, MOCK_MEDIA_ID);

    }
    /**
     * GIVEN: Intent i, with Uri u to MediaPlayerActivity m,
     * WHEN: m is created
     * THEN: i contains u.
     * @exception IllegalAccessException thrown if cannot access private field "token".
     */
    @Test
    public void onCreateSetMediaIdTest() throws IllegalAccessException {
        MediaPlayerActivity mediaPlayerActivity = Robolectric.buildActivity(MediaPlayerActivity.class, intent).create().get();
        MediaSessionCompat.Token token = (MediaSessionCompat.Token) FieldUtils.readDeclaredField(mediaPlayerActivity, "token", true);
        assertEquals(mediaSessionCompat.getSessionToken(), token);
    }
    /**
     * tests activity lifecycle i.e. create -> start -> resume -> pause -> stop -> destroy
     */
    @Test
    public void activityLifeCycleTest() {
        MediaPlayerActivity mediaPlayerActivity = Robolectric.buildActivity(MediaPlayerActivity.class, intent).create().start().resume().pause().stop().destroy().get();
        assertNotNull(mediaPlayerActivity);
    }
}