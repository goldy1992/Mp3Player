package com.example.mike.mp3player.service;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.commons.Constants;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({MediaPlayer.class})
public class MyMediaPlayerAdapterTest extends MediaPlayerAdapterTestBase {

    @Before
    public void setup() {
        PowerMockito.mockStatic(MediaPlayer.class);
        super.setup();
        mediaPlayerAdapter = createMediaPlayerAdapter();
//
        mediaPlayerAdapter.reset(new Uri.Builder().appendPath("abc").build(), null);
        Whitebox.setInternalState(mediaPlayerAdapter, "audioFocusManager", audioFocusManager);
    }

    @Test
    public void testReset() {
        mediaPlayerAdapter.reset(uri, nextUri);
        assertNotNull(mediaPlayerAdapter.getCurrentMediaPlayer());
        assertNotNull(mediaPlayerAdapter.getNextMediaPlayer());
    }

    @Test
    public void testPauseWhilePaused() {
        Whitebox.setInternalState(mediaPlayerAdapter, "currentState", PlaybackStateCompat.STATE_PAUSED);
        mediaPlayerAdapter.pause();
        assertEquals("playback should be paused but state is" + Constants.playbackStateDebugMap.get(mediaPlayerAdapter.getCurrentState()), PlaybackStateCompat.STATE_PAUSED, mediaPlayerAdapter.getCurrentState());
    }

    @Test
    public void testIncreaseSpeed() {
        expectedSpeedChange(1.50f, 0.05f, 1.55f);
    }

    @Test
    public void testPlay() {
        when(audioFocusManager.requestAudioFocus()).thenReturn(true);
        when(mediaPlayerAdapter.getCurrentMediaPlayer().getPlaybackParams()).thenReturn(mock(PlaybackParams.class));
        mediaPlayerAdapter.play();
        assertEquals(PlaybackStateCompat.STATE_PLAYING, mediaPlayerAdapter.getCurrentState());
    }

    @Test
    public void testDecreaseSpeed() {
        expectedSpeedChange(1.50f, 0.05f, 1.45f);
    }

    @Test
    public void testSeekToPrepared() {
        spy(mediaPlayer);
        mediaPlayerAdapter.seekTo(555L);
//        Mockito.verify(mediaPlayer, times(1)).seekTo(555);
    }

    @Test
    public void testSeekToNotPrepared() {
        Whitebox.setInternalState(mediaPlayerAdapter, "isPrepared", false);


        spy(mediaPlayer);
        mediaPlayerAdapter.seekTo(555L);
  //      Mockito.verify(mediaPlayer, times(1)).seekTo(555);
    }

    @Test
    public void testPauseWhilePlaying() {
        Whitebox.setInternalState(mediaPlayerAdapter, "currentState", PlaybackStateCompat.STATE_PLAYING);
        when(mediaPlayer.getPlaybackParams()).thenReturn(new PlaybackParams());
        mediaPlayerAdapter.pause();
        //  when (mockPlaybackParams.getSpeed()).thenReturn(1.2f);
        assertEquals("playback should be paused but state is" + Constants.playbackStateDebugMap.get(mediaPlayerAdapter.getCurrentState()), PlaybackStateCompat.STATE_PAUSED, mediaPlayerAdapter.getCurrentState());
    }

    private void expectedSpeedChange(float originalSpeed, float changeInSpeed, float expectedNewSpeed) {
        PlaybackParams mockPlaybackParams = mock(PlaybackParams.class);
        when(mockPlaybackParams.getSpeed()).thenReturn(originalSpeed);
        when (mediaPlayer.getPlaybackParams()).thenReturn(mockPlaybackParams);

        if (originalSpeed < expectedNewSpeed) {
            mediaPlayerAdapter.increaseSpeed(changeInSpeed);
        } else {
            mediaPlayerAdapter.decreaseSpeed(changeInSpeed);
        }
        float actualSpeed = mediaPlayerAdapter.getCurrentPlaybackSpeed();
        float delta = expectedNewSpeed - actualSpeed;
        assertEquals("Incorrect playback speed, expected " + expectedNewSpeed + " but was " + mediaPlayerAdapter.getCurrentPlaybackSpeed(),
                expectedNewSpeed, actualSpeed, delta);
    }
}