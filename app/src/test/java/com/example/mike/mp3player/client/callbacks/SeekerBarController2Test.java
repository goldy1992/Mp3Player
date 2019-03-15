package com.example.mike.mp3player.client.callbacks;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.SeekerBar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowSeekBar;
import org.robolectric.shadows.ShadowValueAnimator;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = 26, shadows = {ShadowValueAnimator.class, ShadowSeekBar.class})
public class SeekerBarController2Test {

    private Context m_context;
    @Mock
    private MediaControllerAdapter m_mediaControllerAdapter;

    private SeekerBarController2 m_seekerBarController2;
    private SeekerBar m_seekerBar;
    private final long DEFAULT_DURATION = 3000;

    @Before
    public void setUp() {

        m_context = RuntimeEnvironment.systemContext;
        m_seekerBar = new SeekerBar(m_context);
        m_seekerBarController2 = new SeekerBarController2(m_seekerBar, m_mediaControllerAdapter);
        // set default metadata
        m_seekerBarController2.onMetadataChanged(createMetaData(DEFAULT_DURATION));
    }
    @Test
    public void testMetadataChanged() {
        ValueAnimator originalValueAnimator = m_seekerBarController2.getValueAnimator();
        final long DURATION = 1000;
        m_seekerBarController2.onMetadataChanged(createMetaData(DURATION));
        assertValueAnimatorReset(originalValueAnimator);
        ValueAnimator resultValueAnimator = m_seekerBarController2.getValueAnimator();
        assertEquals(DURATION, resultValueAnimator.getDuration());
    }

    @Test
    public void testPlaybackSpeedIncreaseStatePaused() {
        final float SPEED = 1.1f;
        testSpeedChange(SPEED);
    }

    @Test
    public  void testPlaybackSpeedDecreasedSpeedPaused() {
        final float SPEED = 0.75f;
        testSpeedChange(SPEED);
    }
    @After
    public void tearDown() {
    }
    private void testSpeedChange(float speed) {

        final long EXPECTED_DURATION = (long) (DEFAULT_DURATION / speed);
        final PlaybackStateCompat playbackState = createPlaybackState(STATE_PAUSED, 350, speed);
        final ValueAnimator originalValueAnimator = m_seekerBarController2.getValueAnimator();
        m_seekerBarController2.onPlaybackStateChanged(playbackState);
        assertValueAnimatorReset(originalValueAnimator);
        ValueAnimator valueAnimator = m_seekerBarController2.getValueAnimator();
        assertTrue(valueAnimator.isStarted());
        assertTrue(valueAnimator.isPaused());
        long resultDuration = valueAnimator.getDuration();
        String errorMessage = new StringBuilder().append("incorrect duration, expected: ")
                .append(EXPECTED_DURATION)
                .append(" but got ")
                .append(resultDuration)
                .toString();
        assertEquals(errorMessage, EXPECTED_DURATION, resultDuration);
    }

    private void assertValueAnimatorReset(ValueAnimator originalAnimator) {
        ValueAnimator resultValueAnimator = m_seekerBarController2.getValueAnimator();
        assertNotEquals("Value animator should have been recreated", resultValueAnimator, originalAnimator);

    }

    private MediaMetadataCompat createMetaData(long duration) {
        return new MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .build();
    }

    private PlaybackStateCompat createPlaybackState(int state, int position, float speed) {
        return new PlaybackStateCompat.Builder()
                .setState(state, position, speed)
                .build();
    }
}