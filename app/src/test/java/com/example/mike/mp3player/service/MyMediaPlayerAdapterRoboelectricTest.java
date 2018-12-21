package com.example.mike.mp3player.service;

import android.media.MediaPlayer;
import android.support.v4.media.MediaMetadataCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = 26, shadows = {ShadowBundle.class})
public class MyMediaPlayerAdapterRoboelectricTest extends MediaPlayerAdapterTestBase {

    @Test
    public void testGetCurrentMetaData() {
        final int EXPECTED_DURATION = 1234;
        mediaPlayerAdapter = createMediaPlayerAdapter();
        MediaPlayer mediaPlayer = mock (MediaPlayer.class);
        when(mediaPlayer.getDuration()).thenReturn(EXPECTED_DURATION);
        setMediaPlayer(mediaPlayer);
        MediaMetadataCompat mediaMetadataCompat = mediaPlayerAdapter.getCurrentMetaData().build();
        long resultDuration = mediaMetadataCompat.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        assertEquals(EXPECTED_DURATION, resultDuration);

    }

}
