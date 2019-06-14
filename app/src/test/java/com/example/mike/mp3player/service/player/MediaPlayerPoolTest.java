package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.net.Uri;
import android.os.Looper;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.util.DataSource;

@RunWith(RobolectricTestRunner.class)
public class MediaPlayerPoolTest {


    private Context context;
    @Mock
    private Looper looper;
    @Mock
    private Uri uri;

    private MediaPlayerPool m_mediaPlayerPool;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        final String path = "dummy";
        Uri uri = new Uri.Builder().appendPath(path).build();
        ShadowMediaPlayer.addMediaInfo(
                DataSource.toDataSource(context, uri),
                new ShadowMediaPlayer.MediaInfo(100, 10));

        m_mediaPlayerPool = new MediaPlayerPool(context);
    }

    @Test
    public void testReset() {
        m_mediaPlayerPool.reset(uri);
    }

    @After
    public void tearDown() {
    }

}