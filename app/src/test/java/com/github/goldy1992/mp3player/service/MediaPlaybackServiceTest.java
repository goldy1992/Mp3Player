package com.github.goldy1992.mp3player.service;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.media.MediaBrowserServiceCompat;

import com.github.goldy1992.mp3player.service.library.ContentManager;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.util.ArrayList;
import java.util.List;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
@SuppressWarnings("unchecked")
public class MediaPlaybackServiceTest {
    private static final String ACCEPTED_MEDIA_ROOT_ID = "ACCEPTED";
    private static final String REJECTED_MEDIA_ROOT_ID = "REJECTED";
    /** object to test*/
    private TestMediaPlaybackServiceInjector mediaPlaybackService;
    @Mock
    private RootAuthenticator rootAuthenticator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mediaPlaybackService = Robolectric.buildService(TestMediaPlaybackServiceInjector.class).create().get();
        mediaPlaybackService.setHandler(new Handler(getMainLooper()));
        shadowOf(mediaPlaybackService.getWorker().getLooper()).idle();
        mediaPlaybackService.setRootAuthenticator(rootAuthenticator);
    }

    @Test
    public void testGetRoot() {
        MediaBrowserServiceCompat.BrowserRoot browserRoot = new MediaBrowserServiceCompat.BrowserRoot(ACCEPTED_MEDIA_ROOT_ID, null);
        final String clientPackageName = "packageName";
        final int clientUid = 45;
        Bundle rootHints = null;
        when(rootAuthenticator.authenticate(clientPackageName, clientUid, rootHints)).thenReturn(browserRoot);
        MediaBrowserServiceCompat.BrowserRoot result =
                mediaPlaybackService.onGetRoot(clientPackageName, clientUid, rootHints);
        assertNotNull(result);
        assertEquals(ACCEPTED_MEDIA_ROOT_ID, result.getRootId());
    }

    @Test
    public void testOnLoadChildrenWithRejectedRootId() {
        when(rootAuthenticator.rejectRootSubscription(any())).thenReturn(true);
        final String parentId = "aUniqueId";
        MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>> result = mock(MediaBrowserServiceCompat.Result.class);
        mediaPlaybackService.onLoadChildren(parentId, result);
        shadowOf(getMainLooper()).idle();
        verify(result, times(1)).sendResult(null);
    }

    @Test
    public void testOnLoadChildrenWithAcceptedMediaId() {
        final String parentId = "aUniqueId";
        MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>> result = mock(MediaBrowserServiceCompat.Result.class);
        ContentManager contentManager = mediaPlaybackService.getContentManager();
        List<MediaBrowserCompat.MediaItem> mediaItemList = new ArrayList<>();
        when(contentManager.getChildren(any())).thenReturn(mediaItemList);
        mediaPlaybackService.onLoadChildren(parentId, result);
        shadowOf(getMainLooper()).idle();
        verify(result, times(1)).sendResult(mediaItemList);
    }

    @Test
    public void testOnLoadChildrenRejectedMediaId() {
        when(rootAuthenticator.rejectRootSubscription(any())).thenReturn(false);
        MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>> result
                = mock(MediaBrowserServiceCompat.Result.class);


        mediaPlaybackService.onLoadChildren(REJECTED_MEDIA_ROOT_ID, result);
        shadowOf(getMainLooper()).idle();
        // execute all tasks posted to main looper
        //shadowOf(mediaPlaybackService.getWorker().getLooper()).idle();
        verify(result, times(1)).sendResult(any());
    }

    @Test
    public void testOnDestroy() throws IllegalAccessException {
        MediaSessionCompat mediaSessionSpy = spy(mediaPlaybackService.getMediaSession());
        FieldUtils.writeField(mediaPlaybackService, "mediaSession", mediaSessionSpy, true);
        mediaPlaybackService.onDestroy();
        verify(mediaSessionSpy, times(1)).release();
    }

}
