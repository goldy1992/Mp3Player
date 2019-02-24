package com.example.mike.mp3player.service;

import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.commons.Constants;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowMediaPlayer;

import static org.junit.Assert.assertEquals;
@Ignore
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = 26, shadows = {ShadowBundle.class, ShadowMediaPlayer.class})
public class MyMediaPlayerAdapterRoboelectricTest extends MediaPlayerAdapterTestBase {

    @Before
    public void setup() {
        Uri uri = new Uri.Builder().build();
        mediaPlayerAdapter = createMediaPlayerAdapter();
        mediaPlayerAdapter.reset(uri, uri, null);
    }

    @Test
    @Ignore
    public void testGetCurrentMetaData() {
        final int EXPECTED_DURATION = 1234;
        mediaPlayerAdapter = createMediaPlayerAdapter();
        //setMediaPlayer(mediaPlayer);
        MediaMetadataCompat mediaMetadataCompat = mediaPlayerAdapter.getCurrentMetaData().build();
        long resultDuration = mediaMetadataCompat.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        assertEquals(EXPECTED_DURATION, resultDuration);

    }


}
