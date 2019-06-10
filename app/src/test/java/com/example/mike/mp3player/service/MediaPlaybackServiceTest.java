package com.example.mike.mp3player.service;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.media.MediaBrowserServiceCompat;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.UnitTestMikesMp3Player;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.ACCEPTED_MEDIA_ROOT_ID;
import static com.example.mike.mp3player.commons.Constants.PACKAGE_NAME;
import static com.example.mike.mp3player.commons.Constants.REJECTED_MEDIA_ROOT_ID;
import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(application = UnitTestMikesMp3Player.class)
public class MediaPlaybackServiceTest {
    /** object to test*/
    public MediaPlaybackService mediaPlaybackService;
    private Context context;
    @Before
    public void setup() {
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.mediaPlaybackService = Robolectric.buildService(MediaPlaybackService.class).create().get();
    }

    @Test
    public void testGetRootValid() {
        MediaBrowserServiceCompat.BrowserRoot result =
                mediaPlaybackService.onGetRoot(PACKAGE_NAME, 0, null);
        assertEquals(ACCEPTED_MEDIA_ROOT_ID, result.getRootId());
    }

    @Test
    public void testGetRootInvalid() {
        final String badPackageName = "bad.package.name";
        MediaBrowserServiceCompat.BrowserRoot result =
                mediaPlaybackService.onGetRoot(badPackageName, 0, null);
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
        final String parentId = REJECTED_MEDIA_ROOT_ID;
        MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>> result = mock(MediaBrowserServiceCompat.Result.class);
        mediaPlaybackService.onLoadChildren(parentId, result, null);
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
        Bundle extras = new Bundle();
        LibraryRequest libraryRequest = new LibraryRequest(Category.ALBUMS, "an_id");
        extras.putParcelable(REQUEST_OBJECT, libraryRequest);
        mediaPlaybackService.onLoadChildren(parentId, result, extras);
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
