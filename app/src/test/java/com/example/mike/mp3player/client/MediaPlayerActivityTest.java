package com.example.mike.mp3player.client;

import android.content.Intent;

import com.example.mike.mp3player.BuildConfig;
import com.example.mike.mp3player.commons.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MediaPlayerActivityTest {

    private static final String MOCK_MEDIA_ID = "MOCK_MEDIA_ID";
    MediaPlayerActivity mediaPlayerActivity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application.getApplicationContext(), MediaPlayerActivity.class);
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