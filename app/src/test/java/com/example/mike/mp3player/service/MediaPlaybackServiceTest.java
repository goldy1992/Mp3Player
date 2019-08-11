package com.example.mike.mp3player.service;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.media.MediaBrowserServiceCompat;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;


import static com.example.mike.mp3player.commons.Constants.PACKAGE_NAME;

import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MediaPlaybackServiceTest {
    private static final String ACCEPTED_MEDIA_ROOT_ID = "";
    private static final String REJECTED_MEDIA_ROOT_ID = "";
    /** object to test*/
    private TestMediaPlaybackServiceInjector mediaPlaybackService;

    @Before
    public void setup() {
        this.mediaPlaybackService = Robolectric.buildService(TestMediaPlaybackServiceInjector.class).create().get();
    }

    @Test
    public void testGetRootValid() {
        MediaBrowserServiceCompat.BrowserRoot result =
                mediaPlaybackService.onGetRoot(PACKAGE_NAME, 0, null);
        assertNotNull(result);
        assertEquals(ACCEPTED_MEDIA_ROOT_ID, result.getRootId());
    }

    @Test
    public void testGetRootInvalid() {
        final String badPackageName = "bad.package.name";
        MediaBrowserServiceCompat.BrowserRoot result =
                mediaPlaybackService.onGetRoot(badPackageName, 0, null);
        assertNotNull(result);
        assertEquals(REJECTED_MEDIA_ROOT_ID, result.getRootId());
    }

    @Test
    public void testOnLoadChildrenNullExtras() {
        final String parentId = "parentId";
        MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>> result = mock(MediaBrowserServiceCompat.Result.class);
        mediaPlaybackService.onLoadChildren(parentId, result, null);
        verify(result, times(1)).sendResult(null);
    }

    @Test
    public void testOnLoadChildrenRejectedMediaId() {
        MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>> result = mock(MediaBrowserServiceCompat.Result.class);
        mediaPlaybackService.onLoadChildren(REJECTED_MEDIA_ROOT_ID, result);
        verify(result, times(1)).sendResult(null);
    }

    @Test
    public void testOnLoadChildrenNullLibraryRequest() {
        final String parentId = "abcd";
        MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>> result = mock(MediaBrowserServiceCompat.Result.class);
        Bundle extras = new Bundle();
        extras.putString(REQUEST_OBJECT, null);
        mediaPlaybackService.onLoadChildren(parentId, result, extras);
        verify(result, times(1)).sendResult(null);
    }

    @Test
    public void testOnLoadChildrenValidLibraryRequest() {
        final String parentId = "abcd";
        MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>> result = mock(MediaBrowserServiceCompat.Result.class);
        mediaPlaybackService.onLoadChildren(parentId, result);
        verify(result, times(1)).sendResult(any(ArrayList.class));
    }

    @Test
    public void testOnDestroy() throws IllegalAccessException {
        MediaSessionCompat mediaSessionSpy = spy(mediaPlaybackService.getMediaSession());
        FieldUtils.writeField(mediaPlaybackService, "mediaSession", mediaSessionSpy, true);
        mediaPlaybackService.onDestroy();
        verify(mediaSessionSpy, times(1)).release();
    }

}
