package com.example.mike.mp3player.client;

import android.content.Intent;
import android.net.Uri;

import com.example.mike.mp3player.BuildConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MediaPlayerActivityTest {

    @Mock
    Uri uri;

    MediaPlayerActivity mediaPlayerActivity;

    @BeforeEach
    public void setup() {
        Intent intent = new Intent(RuntimeEnvironment.application.getApplicationContext(), MediaPlayerActivity.class);
        intent.putExtra("uri", uri);
        mediaPlayerActivity = Robolectric.buildActivity(MediaPlayerActivity.class, intent).create().get();
    }

    /**
     * GIVEN: Intent i, with Uri u to MediaPlayerActivity m,
     * WHEN: m is created
     * THEN: i contains u.
     */
    @Test
    public void onCreateSetUriTest() {
        assertEquals(uri, mediaPlayerActivity.getSelectedUri());
    }
}