package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.commons.Constants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class MyMediaPlayerAdapterTest {

    @Mock
    Context context;
    @Mock
    Uri uri;
    @Mock
    AudioManager audioManager;

    private MyMediaPlayerAdapter mediaPlayerAdapter;

    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        mediaPlayerAdapter = createMediaPlayerAdapter();
        mediaPlayerAdapter.init(uri);
        assertNotNull("MediaPlayer should not be null after initialisation", mediaPlayerAdapter.getMediaPlayer());
        assertTrue("Didn't initialise MediaPlayerAdapter correctly", mediaPlayerAdapter.isPrepared());
        assertEquals("Initialised into the incorrect state", PlaybackStateCompat.STATE_PAUSED, mediaPlayerAdapter.getCurrentState());
    }

    @Test
    public void testPlayFocusGrantedLowerThanOreo() {
        when(context.getSystemService(Context.AUDIO_SERVICE)).thenReturn(audioManager);
        when(audioManager.requestAudioFocus(any(), anyInt(), anyInt())).thenReturn( AudioManager.AUDIOFOCUS_REQUEST_GRANTED );
        mediaPlayerAdapter.play();
        assertEquals("playback should be playing but state is" + Constants.playbackStateDebugMap.get(mediaPlayerAdapter.getCurrentState()), PlaybackStateCompat.STATE_PLAYING, mediaPlayerAdapter.getCurrentState());
    }

    @Test
    public void testPlayFocusGrantedOreo() {
        when(context.getSystemService(Context.AUDIO_SERVICE)).thenReturn(audioManager);
        when(audioManager.requestAudioFocus(any(), anyInt(), anyInt())).thenReturn( AudioManager.AUDIOFOCUS_REQUEST_GRANTED );
        mediaPlayerAdapter.play();
        assertEquals("playback should be playing but state is" + Constants.playbackStateDebugMap.get(mediaPlayerAdapter.getCurrentState()), PlaybackStateCompat.STATE_PLAYING, mediaPlayerAdapter.getCurrentState());
    }

    @Test
    public void testPauseWhilePlaying() {
        Whitebox.setInternalState(mediaPlayerAdapter, "currentState", PlaybackStateCompat.STATE_PLAYING);
        mediaPlayerAdapter.pause();
        assertEquals("playback should be paused but state is" + Constants.playbackStateDebugMap.get(mediaPlayerAdapter.getCurrentState()), PlaybackStateCompat.STATE_PAUSED, mediaPlayerAdapter.getCurrentState());
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
    public void testDecreaseSpeed() {
        expectedSpeedChange(1.50f, 0.05f, 1.45f);
    }

    @Test
    public void testSeekToPrepared() {
        MediaPlayer mediaPlayer = mock(MediaPlayer.class);
        mediaPlayerAdapter.setMediaPlayer(mediaPlayer);

        spy(mediaPlayer);
        mediaPlayerAdapter.seekTo(555L);
        Mockito.verify(mediaPlayer, times(1)).seekTo(555);
    }

    @Test
    public void testSeekToNotPrepared() {
        Whitebox.setInternalState(mediaPlayerAdapter, "isPrepared", false);
        MediaPlayer mediaPlayer = mock(MediaPlayer.class);
        mediaPlayerAdapter.setMediaPlayer(mediaPlayer);

        spy(mediaPlayer);
        mediaPlayerAdapter.seekTo(555L);
        Mockito.verify(mediaPlayer, times(1)).seekTo(555);
    }

    @Test
    public void getMediaPlayerStateBuilder() {
        final int EXPECTED_STATE = PlaybackStateCompat.STATE_BUFFERING;
        final int EXPECTED_CURRENT_POSITION = 1234;
        final float EXPECTED_SPEED = 123f;

        MediaPlayer mediaPlayer = mock(MediaPlayer.class);
        when(mediaPlayer.getCurrentPosition()).thenReturn(EXPECTED_CURRENT_POSITION);
        PlaybackParams playbackParams = new PlaybackParams();
        playbackParams.setSpeed(EXPECTED_SPEED);

        when(mediaPlayer.getPlaybackParams()).thenReturn(playbackParams);
        mediaPlayer.setPlaybackParams(playbackParams);
        mediaPlayerAdapter.setMediaPlayer(mediaPlayer);
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

    // must try to test this with roboelectric
//    @Test
//    public void testGetCurrentMetaData() {
//        final int EXPECTED_DURATION = 1234;
//        MediaPlayer mediaPlayer = mock (MediaPlayer.class);
//        when(mediaPlayer.getDuration()).thenReturn(EXPECTED_DURATION);
//        mediaPlayerAdapter.setMediaPlayer(mediaPlayer);
//
//        MediaMetadataCompat mediaMetadataCompat = mediaPlayerAdapter.getCurrentMetaData().build();
//        long resultDuration = mediaMetadataCompat.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
//        assertEquals(EXPECTED_DURATION, resultDuration);
//
//    }

    private void expectedSpeedChange(float originalSpeed, float changeInSpeed, float expectedNewSpeed) {
        MediaPlayer mediaPlayer = mock(MediaPlayer.class);

        mediaPlayerAdapter.setMediaPlayer(mediaPlayer);
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


    private MyMediaPlayerAdapter createMediaPlayerAdapter() {
        return new MyMediaPlayerAdapter(context);
    }

}