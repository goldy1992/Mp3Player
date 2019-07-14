package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.service.AudioFocusManager;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.util.DataSource;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class MediaPlayerAdapterTestBase {

    /** mock MediaPlayer.OnCompletionListener */
    @Mock
    MediaPlayer.OnCompletionListener mockOnCompletionListener;
    /** mock MediaPlayer.OnSeekCompleteListener */
    @Mock
    MediaPlayer.OnSeekCompleteListener mockOnSeekCompleteListener;
    @Mock
    AudioFocusManager audioFocusManager;
    /** context */
    Context context;
    @Mock
    Uri uri;
    @Mock
    Uri nextUri;
    MediaPlayerAdapter mediaPlayerAdapter;

    abstract MediaPlayerAdapter createMediaPlayerAdapter();

    /** Test setup method */
    public void setup() throws IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        context = InstrumentationRegistry.getInstrumentation().getContext();
        final String path = "dummy";
        Uri uri = new Uri.Builder().appendPath(path).build();
        ShadowMediaPlayer.addMediaInfo(
                DataSource.toDataSource(context, uri),
                new ShadowMediaPlayer.MediaInfo(100, 10));
        mediaPlayerAdapter = createMediaPlayerAdapter();
        mediaPlayerAdapter.reset(uri, null);
        FieldUtils.writeField(mediaPlayerAdapter, "audioFocusManager", audioFocusManager, true);

    }

    @Test
    public void testReset() {
        mediaPlayerAdapter.reset(uri, nextUri);
        assertNotNull(mediaPlayerAdapter.getCurrentMediaPlayer());
        assertNotNull(mediaPlayerAdapter.getNextMediaPlayer());
    }

    @Test
    public void testPauseWhilePaused() throws IllegalAccessException {
        FieldUtils.writeField(mediaPlayerAdapter, "currentState", PlaybackStateCompat.STATE_PAUSED, true);
        mediaPlayerAdapter.pause();
        assertEquals("playback should be paused but state is" + Constants.playbackStateDebugMap.get(mediaPlayerAdapter.getCurrentState()), PlaybackStateCompat.STATE_PAUSED, mediaPlayerAdapter.getCurrentState());
    }

    @Test
    public void testIncreaseSpeed() {
        expectedSpeedChange(1.50f, 0.05f, 1.55f);
    }

    @Test
    public void testChangeSpeedWhilePlaying() throws IllegalAccessException {
        FieldUtils.writeField(mediaPlayerAdapter, "currentState", STATE_PLAYING, true);
        expectedSpeedChange(1.50f, 0.05f, 1.55f);
        assertEquals(STATE_PLAYING, mediaPlayerAdapter.getCurrentState());
    }


    @Test
    public void testPlay() throws IllegalAccessException {
        FieldUtils.writeField( mediaPlayerAdapter, "audioFocusManager", audioFocusManager, true);
        when(audioFocusManager.requestAudioFocus()).thenReturn(true);
        mediaPlayerAdapter.play();
        assertEquals(STATE_PLAYING, mediaPlayerAdapter.getCurrentState());
    }

    @Test
    public void testDecreaseSpeed() {
        expectedSpeedChange(1.50f, 0.05f, 1.45f);
    }

    @Test
    public void testSeekToPrepared() throws IllegalAccessException {
        MediaPlayer mediaPlayerSpied = spy(mediaPlayerAdapter.getCurrentMediaPlayer());
        FieldUtils.writeField(mediaPlayerAdapter, "currentMediaPlayer", mediaPlayerSpied, true);
        when(audioFocusManager.isInitialised()).thenReturn(true);
        mediaPlayerAdapter.seekTo(555L);
        verify(mediaPlayerSpied, times(1)).seekTo(555);
    }

    @Test
    public void testSeekToNotPrepared() throws IllegalAccessException {
        FieldUtils.writeField(mediaPlayerAdapter, "isPrepared", false, true);
        MediaPlayer mediaPlayerSpied = spy(mediaPlayerAdapter.getCurrentMediaPlayer());
        when(audioFocusManager.isInitialised()).thenReturn(true);
        FieldUtils.writeField(mediaPlayerAdapter, "currentMediaPlayer", mediaPlayerSpied, true);
        mediaPlayerAdapter.seekTo(555L);
        verify(mediaPlayerSpied, times(1)).seekTo(555);
    }

    @Test
    public void testPauseWhilePlaying() throws Exception {
        FieldUtils.writeField(mediaPlayerAdapter, "currentState", STATE_PLAYING, true);
        mediaPlayerAdapter.pause();
        assertEquals("playback should be paused but state is" + Constants.playbackStateDebugMap.get(mediaPlayerAdapter.getCurrentState()), PlaybackStateCompat.STATE_PAUSED, mediaPlayerAdapter.getCurrentState());
    }

    @Test
    public void testOnComplete() throws IllegalAccessException {
        Uri followingUri = mock(Uri.class);
        MediaPlayer mediaPlayerSpied = spy(mediaPlayerAdapter.getCurrentMediaPlayer());
        FieldUtils.writeField(mediaPlayerAdapter, "currentMediaPlayer", mediaPlayerSpied, true);
        mediaPlayerAdapter.onComplete(followingUri);
        verify(mediaPlayerSpied, times(1)).release();
    }
    private void expectedSpeedChange(float originalSpeed, float changeInSpeed, float expectedNewSpeed) {

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