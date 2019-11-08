package com.github.goldy1992.mp3player.client.views.buttons;

import android.support.v4.media.session.PlaybackStateCompat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
public class PlayPauseButtonTest extends MediaButtonTestBase {

    /**
     * Play Pause Button to test
     */
    private PlayPauseButton playPauseButton;

    /**
     * setup
     */
    @Before
    public void setup() {
        super.setup();
        playPauseButton = new PlayPauseButton(context, mediaControllerAdapter, handler);
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
        @PlaybackStateCompat.State int expectedState = STATE_PAUSED;
        playPauseButton.onPlaybackStateChanged(createState(PlaybackStateCompat.STATE_ERROR));
        assertEquals(expectedState, playPauseButton.getState());
    }
    /**
     * GIVEN: a PlayPauseButton in state paused
     * WHEN: the PlayPauseButton is clicked i.e. playPause(View view) is invoked
     * THEN: the mediaControllerAdapter play() method is invoked
     */
    @Test
    public void testClickPlayWhenPaused() {
        playPauseButton.setState(STATE_PAUSED);
        playPauseButton.onClick(null);
        verify(mediaControllerAdapter, times(1)).play();
    }
    /**
     * GIVEN: a PlayPauseButton in state playing
     * WHEN: the PlayPauseButton is clicked i.e. playPause(View view) is invoked
     * THEN: the mediaControllerAdapter pause() method is invoked
     */
    @Test
    public void testClickPauseWhenPlaying() {
        playPauseButton.setState(STATE_PLAYING);
        playPauseButton.onClick(null);
        verify(mediaControllerAdapter, times(1)).pause();
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