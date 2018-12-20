package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.commons.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.util.DataSource;

import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = 26, shadows = {ShadowBundle.class, ShadowMediaPlayer.class})
public class MyMediaPlayerAdapterRoboelectricTest extends MediaPlayerAdapterTestBase {

    ShadowMediaPlayer.MediaInfo mediaInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mediaInfo = new ShadowMediaPlayer.MediaInfo();
        mediaInfo.events = new TreeMap<>();
        ShadowMediaPlayer.addMediaInfo(DataSource.toDataSource(uri.toString()), mediaInfo);
        super.setup();
    }

    @Test
    public void testPlayFocusGrantedOreo() {
        when(context.getSystemService(Context.AUDIO_SERVICE)).thenReturn(audioManager);
        when(audioManager.requestAudioFocus(any())).thenReturn( AudioManager.AUDIOFOCUS_REQUEST_GRANTED );
        mediaPlayerAdapter.play();
        assertEquals("playback should be playing but state is" + Constants.playbackStateDebugMap.get(mediaPlayerAdapter.getCurrentState()), PlaybackStateCompat.STATE_PLAYING, mediaPlayerAdapter.getCurrentState());
    }

    // must try to test this with roboelectric
    @Test
    public void testGetCurrentMetaData() {
        final int EXPECTED_DURATION = 1234;
        MediaPlayer mediaPlayer = mock (MediaPlayer.class);
        when(mediaPlayer.getDuration()).thenReturn(EXPECTED_DURATION);
        mediaPlayerAdapter.setMediaPlayer(mediaPlayer);

        MediaMetadataCompat mediaMetadataCompat = mediaPlayerAdapter.getCurrentMetaData().build();
        long resultDuration = mediaMetadataCompat.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        assertEquals(EXPECTED_DURATION, resultDuration);

    }

}
