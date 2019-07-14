package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.MediaControllerAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(RobolectricTestRunner.class)
public class PlayPauseButtonTest {
    /**
     * Play Pause Button to test
     */
    private PlayPauseButton playPauseButton;
    /**
     * mock onClickListener used for setup
     */
    @Mock
    private MediaControllerAdapter mediaControllerAdapter;
    /**
     * setup
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Context context = InstrumentationRegistry.getInstrumentation().getContext();
        playPauseButton = new PlayPauseButton(context, null, 0, mediaControllerAdapter);
    }

    /**
     * test for the create method.
     */
    @Test
    public void testCreate() {
        assertNotNull(playPauseButton);
        assertEquals(PlaybackStateCompat.STATE_NONE, playPauseButton.getState());
    }
    /**
     * GIVEN: a playPauseButton
     * WHEN: onPlaybackStateChanged is called with STATE_PLAYING
     * THEN: the state of PlayPauseButton is updated to STATE_PLAYING
     */
    @Test
    public void testOnPlaybackStateChangedPlaying() {
         playPauseButton.onPlaybackStateChanged(createState(STATE_PLAYING));
         assertEquals(STATE_PLAYING, playPauseButton.getState());
    }
    /**
     * GIVEN: a playPauseButton
     * WHEN: onPlaybackStateChanged is called with STATE_PAUSED
     * THEN: the state of PlayPauseButton is updated to STATE_PAUSED
     */
    @Test
    public void testOnPlaybackStateChangedPaused() {
        playPauseButton.onPlaybackStateChanged(createState(PlaybackStateCompat.STATE_PAUSED));
        assertEquals(STATE_PAUSED, playPauseButton.getState());
    }
    /**
     * GIVEN: a playPauseButton in state S
     * WHEN: onPlaybackStateChanged is called with an alternative state
     * THEN: the state of PlayPauseButton remains as state S
     */
    @Test
    public void testOnPlaybackStateChangedOtherState() {
        @PlaybackStateCompat.State int originalState = playPauseButton.getState();
        playPauseButton.onPlaybackStateChanged(createState(PlaybackStateCompat.STATE_ERROR));
        assertEquals(originalState, playPauseButton.getState());
    }
    /**
     *  util method to create a PlaybackStateCompat
     * @param playbackState the playback state
     * @return a PlaybackStateCompat object with state playbackState
     */
    private PlaybackStateCompat createState(@PlaybackStateCompat.State int playbackState) {
        return new PlaybackStateCompat.Builder()
                .setState(playbackState, 0, 0)
                .build();

    }
}