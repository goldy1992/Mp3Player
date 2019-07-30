package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.buttons.ShuffleButton;
import com.example.mike.mp3player.commons.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL;
import static android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
public class ShuffleButtonTest {

    @Mock
    private MediaControllerAdapter mediaControllerAdapter;
    @Mock
    private Handler handler;
    private ShuffleButton shuffleButton;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.shuffleButton = new ShuffleButton(context, mediaControllerAdapter, handler);
    }

    @Test
    public void updateStateShuffleModeAll() {
        @PlaybackStateCompat.ShuffleMode final int shuffleMode = SHUFFLE_MODE_ALL;
        this.shuffleButton.updateState(shuffleMode);
        assertEquals(shuffleMode, shuffleButton.getShuffleMode());
    }

    @Test
    public void updateStateNotShuffleModeAll() {
        @PlaybackStateCompat.ShuffleMode final int shuffleMode = SHUFFLE_MODE_NONE;
        this.shuffleButton.updateState(shuffleMode);
        assertEquals(shuffleMode, shuffleButton.getShuffleMode());
    }

    @Test
    public void testToggleShuffleChangeToShuffleAll() {
        @PlaybackStateCompat.ShuffleMode final int currentShuffleMode = SHUFFLE_MODE_NONE;
        @PlaybackStateCompat.ShuffleMode final int expectedShuffleMode = SHUFFLE_MODE_ALL;
        shuffleButton.setShuffleMode(currentShuffleMode);
        shuffleButton.toggleShuffle(mock(View.class));
        verify(mediaControllerAdapter, times(1)).setShuffleMode(expectedShuffleMode);
        assertEquals(expectedShuffleMode, shuffleButton.getShuffleMode());
    }

    @Test
    public void testToggleShuffleChangeToShuffleNone() {
        @PlaybackStateCompat.ShuffleMode final int currentShuffleMode = SHUFFLE_MODE_ALL;
        @PlaybackStateCompat.ShuffleMode final int expectedShuffleMode = SHUFFLE_MODE_NONE;
        shuffleButton.setShuffleMode(currentShuffleMode);
        shuffleButton.toggleShuffle(mock(View.class));
        verify(mediaControllerAdapter, times(1)).setShuffleMode(expectedShuffleMode);
        assertEquals(expectedShuffleMode, shuffleButton.getShuffleMode());
    }

    @Test
    public void testOnPlaybackStateChanged() {
        @PlaybackStateCompat.ShuffleMode final int expectedShuffleMode = SHUFFLE_MODE_NONE;
        Bundle extras = new Bundle();
        extras.putInt(Constants.SHUFFLE_MODE, expectedShuffleMode);
        PlaybackStateCompat playbackState = new PlaybackStateCompat.Builder().setExtras(extras).build();
        shuffleButton.onPlaybackStateChanged(playbackState);

        assertEquals(expectedShuffleMode, shuffleButton.getShuffleMode());
    }

}