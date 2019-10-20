package com.example.mike.mp3player.service.player;

import android.os.Handler;
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
import org.robolectric.annotation.LooperMode;

import static android.os.Looper.getMainLooper;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(RobolectricTestRunner.class)
@LooperMode(PAUSED)
public class IncreaseSpeedProviderTest extends SpeedProviderTestBase {

    private IncreaseSpeedProvider increaseSpeedProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup();
        this.increaseSpeedProvider = new IncreaseSpeedProvider(handler);
    }

    @Test
    public void testGetCustomAction() {
        PlaybackStateCompat.CustomAction customAction = increaseSpeedProvider.getCustomAction(exoPlayer);
        assertEquals(INCREASE_PLAYBACK_SPEED, customAction.getAction());
        assertEquals(INCREASE_PLAYBACK_SPEED, customAction.getName());
    }

    @Test
    public void testIncreaseSpeed() {
        final float currentSpeed = 1.0f;
        final float expectedSpeed = 1.05f;
        when(exoPlayer.getPlaybackParameters()).thenReturn(new PlaybackParameters(currentSpeed));
        increaseSpeedProvider.onCustomAction(exoPlayer, controlDispatcher, INCREASE_PLAYBACK_SPEED, null);
        shadowOf(getMainLooper()).idle();
        verify(exoPlayer, times(1)).setPlaybackParameters(captor.capture());

        PlaybackParameters playbackParameters = captor.getValue();
        assertEquals(expectedSpeed, playbackParameters.speed, 0.0f);
    }

    /**
     * The speed SHOULD NOT increase because it would over take the maximum
     */
    @Test
    public void testIncreaseSpeedInvalidSpeed() {
        final float currentSpeed = 1.98f;
        final float expectedSpeed = 1.98f;
        when(exoPlayer.getPlaybackParameters()).thenReturn(new PlaybackParameters(currentSpeed));
        increaseSpeedProvider.onCustomAction(exoPlayer, controlDispatcher, INCREASE_PLAYBACK_SPEED, null);
        verify(exoPlayer, never()).setPlaybackParameters(any());

        PlaybackParameters playbackParameters = exoPlayer.getPlaybackParameters();
        assertEquals(expectedSpeed, playbackParameters.speed, 0.00f);
    }

}