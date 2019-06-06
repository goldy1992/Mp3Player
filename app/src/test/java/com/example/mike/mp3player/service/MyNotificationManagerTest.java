package com.example.mike.mp3player.service;

import android.app.Notification;
import android.content.Context;
import android.media.MediaMetadata;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class MyNotificationManagerTest
{
    private static final int OREO = 28;
    private static final int MARSHMALLOW = 23;
    private MyNotificationManager myNotificationManager;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        MediaSessionCompat mediaSession = new MediaSessionCompat(context, "MEDIA_SESSION");
        this.myNotificationManager = new MyNotificationManager(context, mediaSession.getSessionToken());
    }

    @Test
    @Config(sdk = OREO)
    public void testGetOreoNotification() {
        final String title = "title";
        Notification result = myNotificationManager.getNotification(createMetaData(title), createPlaybackState());
        assertNotNull(result);

        Bundle extras = result.extras;
        assertEquals(title, extras.getString(Notification.EXTRA_TITLE));
        assertEquals("There should be 3 actions, Skip to previous, play/pause, skip to next",
                3, result.actions.length);
    }

    private MediaMetadataCompat createMetaData(String title) {
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .build();
    }

    private PlaybackStateCompat createPlaybackState() {
        return new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0L, 0f )
                .build();
    }
}