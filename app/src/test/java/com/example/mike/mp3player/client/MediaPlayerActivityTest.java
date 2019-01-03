package com.example.mike.mp3player.client;

import android.app.Application;
import android.content.Intent;
import android.media.session.MediaSession;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.BuildConfig;
import com.example.mike.mp3player.commons.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config()
public class MediaPlayerActivityTest {

    private static final String MOCK_MEDIA_ID = "MOCK_MEDIA_ID";

    @Mock
    private MediaSessionCompat.Token mockToken;

    MediaPlayerActivity mediaPlayerActivity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MediaPlayerActivity.class);
        intent.putExtra(Constants.MEDIA_SESSION, mockToken);
        intent.putExtra(Constants.MEDIA_ID, MOCK_MEDIA_ID);
        mediaPlayerActivity = Robolectric.buildActivity(MediaPlayerActivity.class, intent).create().get();
    }

    @Before
    public void oldSetup() {
        setup();
    }

    /**
     * GIVEN: Intent i, with Uri u to MediaPlayerActivity m,
     * WHEN: m is created
     * THEN: i contains u.
     */
    @Test
    public void onCreateSetMediaIdTest() {
       assertEquals(MOCK_MEDIA_ID, mediaPlayerActivity.getMediaId());
    }
}