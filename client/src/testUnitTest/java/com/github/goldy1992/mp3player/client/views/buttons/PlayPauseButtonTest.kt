package com.github.goldy1992.mp3player.client.views.buttons

import android.support.v4.media.session.PlaybackStateCompat
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlayPauseButtonTest : MediaButtonTestBase() {
    /**
     * Play Pause Button to test
     */
    private lateinit var playPauseButton: PlayPauseButton

    /**
     * setup
     */
    @Before
    public override fun setup() {
        super.setup()
        playPauseButton = PlayPauseButton(context, mediaControllerAdapter)
        playPauseButton.init(view)
    }

    /**
     * test for the create method.
     */
    @Test
    fun testCreate() {
        Assert.assertNotNull(playPauseButton)
        Assert.assertEquals(2, playPauseButton.state)
    }

    /**
     * GIVEN: a playPauseButton
     * WHEN: onPlaybackStateChanged is called with STATE_PLAYING
     * THEN: the state of PlayPauseButton is updated to STATE_PLAYING
     */
    @Test
    fun testOnChangedPlaying() {
        playPauseButton.onChanged(createState(PlaybackStateCompat.STATE_PLAYING))
        Assert.assertEquals(PlaybackStateCompat.STATE_PLAYING, playPauseButton.state)
    }

    /**
     * GIVEN: a playPauseButton
     * WHEN: onPlaybackStateChanged is called with STATE_PAUSED
     * THEN: the state of PlayPauseButton is updated to STATE_PAUSED
     */
    @Test
    fun testOnChangedPaused() {
        playPauseButton.onChanged(createState(PlaybackStateCompat.STATE_PAUSED))
        Assert.assertEquals(PlaybackStateCompat.STATE_PAUSED, playPauseButton.state)
    }

    /**
     * GIVEN: a playPauseButton in state S
     * WHEN: onPlaybackStateChanged is called with an alternative state
     * THEN: the state of PlayPauseButton remains as state S
     */
    @Test
    fun testOnChangedOtherState() {
        @PlaybackStateCompat.State val expectedState = PlaybackStateCompat.STATE_PAUSED
        playPauseButton.onChanged(createState(PlaybackStateCompat.STATE_ERROR))
        Assert.assertEquals(expectedState, playPauseButton.state)
    }

    /**
     * GIVEN: a PlayPauseButton in state paused
     * WHEN: the PlayPauseButton is clicked i.e. playPause(View view) is invoked
     * THEN: the mediaControllerAdapter play() method is invoked
     */
    @Test
    fun testClickPlayWhenPaused() {
        playPauseButton.state = PlaybackStateCompat.STATE_PAUSED
        playPauseButton.onClick(null)
        verify(mediaControllerAdapter, times(1)).play()
    }

    /**
     * GIVEN: a PlayPauseButton in state playing
     * WHEN: the PlayPauseButton is clicked i.e. playPause(View view) is invoked
     * THEN: the mediaControllerAdapter pause() method is invoked
     */
    @Test
    fun testClickPauseWhenPlaying() {
        playPauseButton.state = PlaybackStateCompat.STATE_PLAYING
        playPauseButton.onClick(null)
        verify(mediaControllerAdapter, times(1)).pause()
    }

    /**
     * util method to create a PlaybackStateCompat
     * @param playbackState the playback state
     * @return a PlaybackStateCompat object with state playbackState
     */
    private fun createState(@PlaybackStateCompat.State playbackState: Int): PlaybackStateCompat {
        return PlaybackStateCompat.Builder()
                .setState(playbackState, 0, 0f)
                .build()
    }
}