package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.commons.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MediaPlayer.class})
public class MyMediaPlayerAdapterTest extends MediaPlayerAdapterTestBase {

    @Before
    public void setup() {
        super.setup();
        PowerMockito.mockStatic(MediaPlayer.class);
        mediaPlayerAdapter = createMediaPlayerAdapter();
        PowerMockito.when(MediaPlayer.create(any(Context.class), any(Uri.class))).thenReturn(mediaPlayer);
        mediaPlayerAdapter.reset(uri, null, mock(MediaPlayer.OnCompletionListener.class));
        Whitebox.setInternalState(mediaPlayerAdapter, "audioFocusManager", audioFocusManager);
    }

    @Test
    public void testReset() {
        mediaPlayerAdapter.reset(uri, nextUri, mock(MediaPlayer.OnCompletionListener.class));
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
        Whitebox.setInternalState(mediaPlayerAdapter, "isPrepared", true);
        when(audioFocusManager.requestAudioFocus()).thenReturn(true);
        when(mediaPlayer.getPlaybackParams()).thenReturn(new PlaybackParams());
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
        Mockito.verify(mediaPlayer, times(1)).seekTo(555);
    }

    @Test
    public void testSeekToNotPrepared() {
        Whitebox.setInternalState(mediaPlayerAdapter, "isPrepared", false);


        spy(mediaPlayer);
        mediaPlayerAdapter.seekTo(555L);
        Mockito.verify(mediaPlayer, times(1)).seekTo(555);
    }

    @Test
    public void getMediaPlayerStateBuilder() {
        final int EXPECTED_STATE = PlaybackStateCompat.STATE_BUFFERING;
        final int EXPECTED_CURRENT_POSITION = 1234;
        final float EXPECTED_SPEED = 123f;

        when(mediaPlayer.getCurrentPosition()).thenReturn(EXPECTED_CURRENT_POSITION);
        PlaybackParams playbackParams = new PlaybackParams();
        playbackParams.setSpeed(EXPECTED_SPEED);
        when(mediaPlayer.getPlaybackParams()).thenReturn(playbackParams);
        mediaPlayer.setPlaybackParams(playbackParams);
        Whitebox.setInternalState(mediaPlayerAdapter, "currentState", EXPECTED_STATE);

        PlaybackStateCompat result = mediaPlayerAdapter.getMediaPlayerState();
        long resultPosition = result.getPosition();
        float resultSpeed = result.getPlaybackSpeed();
        float speedDiff = EXPECTED_SPEED - resultSpeed;
        int resultState = result.getState();
        assertEquals(EXPECTED_CURRENT_POSITION, (int)resultPosition);
        assertEquals(EXPECTED_SPEED, resultSpeed, speedDiff);
        assertEquals(EXPECTED_STATE, resultState);
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