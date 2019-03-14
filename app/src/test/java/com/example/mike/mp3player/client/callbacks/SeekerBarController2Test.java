package com.example.mike.mp3player.client.callbacks;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.media.MediaMetadataCompat;
import android.widget.SeekBar;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.SeekerBar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContextImpl;
import org.robolectric.shadows.ShadowSeekBar;
import org.robolectric.shadows.ShadowValueAnimator;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = 26, shadows = {ShadowValueAnimator.class, ShadowSeekBar.class})
public class SeekerBarController2Test {

    private Context m_context;
    @Mock
    private MediaControllerAdapter m_mediaControllerAdapter;

    private SeekerBarController2 m_seekerBarController2;
    private SeekerBar m_seekerBar;

    @Before
    public void setUp() {

        m_context = RuntimeEnvironment.systemContext;
        m_seekerBar = new SeekerBar(m_context);
        m_seekerBarController2 = new SeekerBarController2(m_seekerBar, m_mediaControllerAdapter);
    }
    @Test
    public void testMetadataChanged() {
        final long DURATION = 1000;
        m_seekerBarController2.onMetadataChanged(createMetaData(DURATION));
        ValueAnimator valueAnimator = m_seekerBarController2.getValueAnimator();
        assertEquals(DURATION, valueAnimator.getDuration());
    }

    @After
    public void tearDown() {
    }

    private MediaMetadataCompat createMetaData(long duration) {
        return new MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .build();
    }
}