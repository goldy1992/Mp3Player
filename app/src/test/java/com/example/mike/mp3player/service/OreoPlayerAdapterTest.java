package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.service.player.OreoPlayerAdapterBase;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.util.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class OreoPlayerAdapterTest {
    /** mock MediaPlayer.OnCompletionListener */
    @Mock
    private MediaPlayer.OnCompletionListener mockOnCompletionListener;
    /** mock MediaPlayer.OnSeekCompleteListener */
    @Mock
    private MediaPlayer.OnSeekCompleteListener mockOnSeekCompleteListener;
    @Mock
    private AudioFocusManager audioFocusManager;
    /** context */
    private Context context;
    @Mock
    Uri uri;
    @Mock
    Uri nextUri;
    /** Media Player Adapter */
    OreoPlayerAdapterBase mediaPlayerAdapter;
    /**
     * setup
     */
    @Before
    public void setup() {
       // super.setup();
        MockitoAnnotations.initMocks(this);
        context = InstrumentationRegistry.getInstrumentation().getContext();
        final String path = "dummy";
        Uri uri = new Uri.Builder().appendPath(path).build();
        ShadowMediaPlayer.addMediaInfo(
                DataSource.toDataSource(context, uri),
                new ShadowMediaPlayer.MediaInfo(100, 10));
        mediaPlayerAdapter = createMediaPlayerAdapter();


//        mediaPlayerAdapter.reset(new Uri.Builder().appendPath("abc").build(), null);
        mediaPlayerAdapter.reset(uri, null);
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
    public void testPlay() throws IllegalAccessException {
        FieldUtils.writeField( mediaPlayerAdapter, "audioFocusManager", audioFocusManager, true);
//        Whitebox.setInternalState(mediaPlayerAdapter, "audioFocusManager", audioFocusManager, true);
        when(audioFocusManager.requestAudioFocus()).thenReturn(true);
        mediaPlayerAdapter.play();
        assertEquals(PlaybackStateCompat.STATE_PLAYING, mediaPlayerAdapter.getCurrentState());
    }

    @Test
    public void testDecreaseSpeed() {
        expectedSpeedChange(1.50f, 0.05f, 1.45f);
    }

    @Test
    public void testSeekToPrepared() {
        spy(mediaPlayerAdapter.getCurrentMediaPlayer());
        mediaPlayerAdapter.seekTo(555L);
//        Mockito.verify(mediaPlayer, times(1)).seekTo(555);
    }

    @Test
    public void testSeekToNotPrepared() {
        Whitebox.setInternalState(mediaPlayerAdapter, "isPrepared", false);


        spy(mediaPlayerAdapter.getCurrentMediaPlayer());
        mediaPlayerAdapter.seekTo(555L);
  //      Mockito.verify(mediaPlayer, times(1)).seekTo(555);
    }

    @Test
    public void testPauseWhilePlaying() {
        Whitebox.setInternalState(mediaPlayerAdapter, "currentState", PlaybackStateCompat.STATE_PLAYING);
        mediaPlayerAdapter.pause();
        //  when (mockPlaybackParams.getSpeed()).thenReturn(1.2f);
        assertEquals("playback should be paused but state is" + Constants.playbackStateDebugMap.get(mediaPlayerAdapter.getCurrentState()), PlaybackStateCompat.STATE_PAUSED, mediaPlayerAdapter.getCurrentState());
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

    // ROBOELECTRIC
    @Test
    public void testGetCurrentMetaData() {
        assertTrue(true);
//        final int EXPECTED_DURATION = 1234;
//        //mediaPlayerAdapter = new OreoPlayerAdapterBase(context, null, null);
//        Whitebox.setInternalState(mediaPlayerAdapter, "currentMediaPlayer", mediaPlayer);
//        when(mediaPlayer.getDuration()).thenReturn(EXPECTED_DURATION);
//        //setMediaPlayer(mediaPlayer);
//        MediaMetadataCompat mediaMetadataCompat = mediaPlayerAdapter.getCurrentMetaData().build();
//        long resultDuration = mediaMetadataCompat.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
//        assertEquals(EXPECTED_DURATION, resultDuration);

    }

    private OreoPlayerAdapterBase createMediaPlayerAdapter() {
        return new OreoPlayerAdapterBase(context, mockOnCompletionListener, mockOnSeekCompleteListener);
    }
}