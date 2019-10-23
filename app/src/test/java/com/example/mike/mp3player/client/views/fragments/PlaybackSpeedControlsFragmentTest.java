package com.example.mike.mp3player.client.views.fragments;

import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.testing.FragmentScenario;

import com.example.mike.mp3player.client.MediaControllerAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static android.os.Looper.getMainLooper;
import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class PlaybackSpeedControlsFragmentTest extends FragmentTestBase<PlaybackSpeedControlsFragment> {

    @Mock
    private MediaControllerAdapter mediaControllerAdapter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup(PlaybackSpeedControlsFragment.class, true);
    }

    @Test
    public void testIncreaseSpeed() {
        FragmentScenario.FragmentAction<PlaybackSpeedControlsFragment> fragmentAction =
                this::increaseSpeed;
        this.performAction(fragmentAction);
    }

    @Test
    public void testDecreaseSpeed() {
        FragmentScenario.FragmentAction<PlaybackSpeedControlsFragment> fragmentAction =
                this::decreaseSpeed;
        this.performAction(fragmentAction);
    }

    private void increaseSpeed(PlaybackSpeedControlsFragment playbackSpeedControlsFragment) {
        playbackSpeedControlsFragment.setMediaControllerAdapter(mediaControllerAdapter);
        playbackSpeedControlsFragment.mainUpdater = new Handler(Looper.getMainLooper());
        playbackSpeedControlsFragment.increasePlaybackSpeed();
        shadowOf(playbackSpeedControlsFragment.worker.getLooper()).idle();
        verify(mediaControllerAdapter, times(1)).sendCustomAction(eq(INCREASE_PLAYBACK_SPEED), any());
    }

    private void decreaseSpeed(PlaybackSpeedControlsFragment playbackSpeedControlsFragment) {
        playbackSpeedControlsFragment.setMediaControllerAdapter(mediaControllerAdapter);
        playbackSpeedControlsFragment.mainUpdater = new Handler(Looper.getMainLooper());
        playbackSpeedControlsFragment.decreasePlaybackSpeed();
        shadowOf(playbackSpeedControlsFragment.worker.getLooper()).idle();
        verify(mediaControllerAdapter, times(1)).sendCustomAction(eq(DECREASE_PLAYBACK_SPEED), any());
    }
}