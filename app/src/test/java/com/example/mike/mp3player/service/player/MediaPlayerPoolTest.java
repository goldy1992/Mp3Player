package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;

import androidx.test.platform.app.InstrumentationRegistry;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.util.DataSource;

import java.lang.reflect.Field;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MediaPlayerPoolTest {


    private Context context;

    private static final int ZERO_CAPACITY = 0;

    private Uri uri;
    private int expectedQueueSize;
    private MediaPlayerPool m_mediaPlayerPool;

    @Before
    public void setUp() throws IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        final String path = "dummy";
        this.uri = new Uri.Builder().appendPath(path).build();
        ShadowMediaPlayer.addMediaInfo(
                DataSource.toDataSource(context, uri),
                new ShadowMediaPlayer.MediaInfo(100, 10));

        this.m_mediaPlayerPool = new MediaPlayerPool(context);
        this.expectedQueueSize = (int) FieldUtils.readDeclaredStaticField(MediaPlayerPool.class, "DEFAULT_QUEUE_CAPACITY");
    }

    @Test
    public void testResetDefaultQueueSize() {
        m_mediaPlayerPool.reset(uri);
        int resultSize = m_mediaPlayerPool.getQueue().size();
        assertEquals(expectedQueueSize, resultSize);
        assertEquals(ZERO_CAPACITY, m_mediaPlayerPool.getQueue().remainingCapacity());
    }

    @Test
    public void testResetCustomQueueSize() {
        this.expectedQueueSize = 2;
        m_mediaPlayerPool = new MediaPlayerPool(context, expectedQueueSize);
        m_mediaPlayerPool.reset(uri);
        int resultSize = m_mediaPlayerPool.getQueue().size();
        assertEquals(expectedQueueSize, resultSize);
        assertEquals(ZERO_CAPACITY, m_mediaPlayerPool.getQueue().remainingCapacity());
    }

    @Test
    public void testTake() {
        this.expectedQueueSize -= 1; // because we are taking 1 from a full queue
        m_mediaPlayerPool.reset(uri);
        MediaPlayer mediaPlayer = m_mediaPlayerPool.take();
        assertNotNull(mediaPlayer);
        /* using >= because we add a new mediaPlayer once we remove the old one, this is done
        asynchronously therefore could have been added or maybe is still in the process of being
        added */
        assertTrue( m_mediaPlayerPool.getQueue().size() >= expectedQueueSize);
    }

    public void testTakeFromEmptyQueue() {
        m_mediaPlayerPool.reset(uri);
        m_mediaPlayerPool.getQueue().clear();
        assertNull(m_mediaPlayerPool.take());
        assertEquals(1, m_mediaPlayerPool.getQueue().size());
    }

    @Test
    public void testTakeThatThrowsInterruptedException() throws InterruptedException {
        m_mediaPlayerPool.reset(uri);
        ArrayBlockingQueue<MediaPlayer> spyArrayBlockingQueue = spy(m_mediaPlayerPool.getQueue());
        doThrow(InterruptedException.class).when(spyArrayBlockingQueue).poll(anyLong(), eq(TimeUnit.MILLISECONDS));
        m_mediaPlayerPool.setQueue(spyArrayBlockingQueue);
        assertNull(m_mediaPlayerPool.take());
    }

    @Test
    public void testTakeThatThrowsIllegalAccessException() throws InterruptedException {
        m_mediaPlayerPool.reset(uri);
        ArrayBlockingQueue<MediaPlayer> mockArrayBlockingQueue = mock(ArrayBlockingQueue.class);
        doThrow(IllegalStateException.class).when(mockArrayBlockingQueue).poll(anyLong(), eq(TimeUnit.MILLISECONDS));
        m_mediaPlayerPool.setQueue(mockArrayBlockingQueue);
        assertNull(m_mediaPlayerPool.take());
    }

    @Test
    public void testTakeWithMediaPlayerPoolNotSet() {
        assertNull(m_mediaPlayerPool.take());
    }

    @After
    public void tearDown() {
    }

}