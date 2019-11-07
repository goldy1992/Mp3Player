package com.example.mike.mp3player.service.player;

import android.content.ContentResolver;
import android.net.Uri;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MediaSourceFactoryTest {

    private MediaSourceFactory mediaSourceFactory;

    @Mock
    private FileDataSource fileDataSource;

    @Mock
    private ContentDataSource contentDataSource;

    @Mock
    private Uri testUri;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mediaSourceFactory = new MediaSourceFactory(fileDataSource, contentDataSource);
    }

    @Test
    public void testCreateContentMediaSource() throws ContentDataSource.ContentDataSourceException,
            FileDataSource.FileDataSourceException {
        when(testUri.getScheme()).thenReturn(ContentResolver.SCHEME_CONTENT);
        MediaSource result = mediaSourceFactory.createMediaSource(testUri);
        assertNotNull(result);
        verify(contentDataSource, times(1)).open(any());
        verify(fileDataSource, never()).open(any());
    }

    @Test
    public void testCreateFileMediaSource() throws ContentDataSource.ContentDataSourceException,
            FileDataSource.FileDataSourceException {
        when(testUri.getScheme()).thenReturn(ContentResolver.SCHEME_FILE);
        MediaSource result = mediaSourceFactory.createMediaSource(testUri);
        assertNotNull(result);
        verify(fileDataSource, times(1)).open(any());
        verify(contentDataSource, never()).open(any());
    }

    @Test
    public void returnNullOnException() throws ContentDataSource.ContentDataSourceException {
        when(contentDataSource.open(any())).thenThrow(new ContentDataSource.ContentDataSourceException(new IOException()));
        assertNull(mediaSourceFactory.createMediaSource(testUri));
    }

}