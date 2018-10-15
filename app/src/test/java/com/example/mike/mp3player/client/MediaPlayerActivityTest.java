package com.example.mike.mp3player.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.mike.mp3player.BuildConfig;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MediaPlayerActivityTest {

    MediaPlayerActivity mediaPlayerActivity;

    @BeforeClass
    public static void setup() {
    }

    /**
     * GIVEN: Intent i, with Uri u to MediaPlayerActivity m,
     * WHEN: m is created
     * THEN: i contains u.
     */
    @Test
    public void onCreateSetUriTest() {
        // GIVEN
        Uri uri = mock(Uri.class);
        Intent intent = new Intent(RuntimeEnvironment.application.getApplicationContext(), MediaPlayerActivity.class);
        intent.putExtra("uri", uri);
        // WHEN
        mediaPlayerActivity = Robolectric.buildActivity(MediaPlayerActivity.class, intent).create().get();
        //THEN:
        assertEquals(uri, mediaPlayerActivity.getSelectedUri());
    }

    @Test
    public void onStartTest() {
    }

    @Test
    public void onStopTest() {
    }
}