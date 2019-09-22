package com.example.mike.mp3player.service.player;

import android.support.v4.media.session.PlaybackStateCompat;

import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class DecreaseSpeedProviderTest {

    @Mock
    private ExoPlayer exoPlayer;

    @Captor
    ArgumentCaptor<PlaybackParameters> captor;

    @Mock
    private ControlDispatcher controlDispatcher;

    private DecreaseSpeedProvider decreaseSpeedProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.decreaseSpeedProvider = new DecreaseSpeedProvider();
    }

    @Test
    public void testGetCustomAction() {
        PlaybackStateCompat.CustomAction customAction = decreaseSpeedProvider.getCustomAction(exoPlayer);
        assertEquals(DECREASE_PLAYBACK_SPEED, customAction.getAction());
        assertEquals(DECREASE_PLAYBACK_SPEED, customAction.getName());
    }

    @Test
    public void testDecreaseSpeed() {
        final float currentSpeed = 1.0f;
        final float expectedSpeed = 0.95f;
        when(exoPlayer.getPlaybackParameters()).thenReturn(new PlaybackParameters(currentSpeed));
        decreaseSpeedProvider.onCustomAction(exoPlayer, controlDispatcher, DECREASE_PLAYBACK_SPEED, null);
        verify(exoPlayer, times(1)).setPlaybackParameters(captor.capture());

        PlaybackParameters playbackParameters = captor.getValue();
        assertEquals(expectedSpeed, playbackParameters.speed, 0.0f);
    }

    /**
     * The speed SHOULD NOT decrease because it would under take the minimum
     */
    @Test
    public void testDecreaseSpeedInvalidSpeed() {
        final float currentSpeed = 0.27f;
        final float expectedSpeed = 0.27f;
        when(exoPlayer.getPlaybackParameters()).thenReturn(new PlaybackParameters(currentSpeed));
        decreaseSpeedProvider.onCustomAction(exoPlayer, controlDispatcher, DECREASE_PLAYBACK_SPEED, null);
        verify(exoPlayer, never()).setPlaybackParameters(any());

        PlaybackParameters playbackParameters = exoPlayer.getPlaybackParameters();
        assertEquals(expectedSpeed, playbackParameters.speed, 0.00f);
    }

}