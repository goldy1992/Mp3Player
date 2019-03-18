package com.example.mike.mp3player.service;

import android.support.v4.media.MediaMetadataCompat;

import com.example.mike.mp3player.service.player.MyMediaPlayerAdapter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowMediaPlayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = 26, shadows = {ShadowBundle.class, ShadowMediaPlayer.class})
public class MyMediaPlayerAdapterRoboelectricTest extends MediaPlayerAdapterTestBase {

    @Before
    public void setup() {
        //super.setup();
    }

    @Test
    public void testGetCurrentMetaData() {
        assertTrue(true);
//        final int EXPECTED_DURATION = 1234;
//        //mediaPlayerAdapter = new MyMediaPlayerAdapter(context, null, null);
//        Whitebox.setInternalState(mediaPlayerAdapter, "currentMediaPlayer", mediaPlayer);
//        when(mediaPlayer.getDuration()).thenReturn(EXPECTED_DURATION);
//        //setMediaPlayer(mediaPlayer);
//        MediaMetadataCompat mediaMetadataCompat = mediaPlayerAdapter.getCurrentMetaData().build();
//        long resultDuration = mediaMetadataCompat.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
//        assertEquals(EXPECTED_DURATION, resultDuration);

    }


}
