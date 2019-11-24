package com.github.goldy1992.mp3player.client.callbacks.subscription;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class MediaIdSubscriptionCallbackTest {

    private MediaIdSubscriptionCallback mediaIdSubscriptionCallback;

    @Mock
    private MediaBrowserResponseListener mediaBrowserResponseListener;

    private List<MediaItem> mediaItemList;

    private MediaItem mockMediaItem;

    @Captor
    ArgumentCaptor<ArrayList<MediaItem>> captor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Handler handler = new Handler(Looper.getMainLooper());
        this.mockMediaItem = mock(MediaItem.class);
        this.mediaIdSubscriptionCallback = new MediaIdSubscriptionCallback(handler);
        this.mediaItemList = Collections.singletonList(mockMediaItem);
    }

    @Test
    public void testRegisterListener() {
        final String key = "KEY";
       assertTrue(mediaIdSubscriptionCallback.getMediaBrowserResponseListeners().isEmpty());
       mediaIdSubscriptionCallback.registerMediaBrowserResponseListener(key, mediaBrowserResponseListener);
       // assert size is now 1
        final int restultSize = mediaIdSubscriptionCallback.getMediaBrowserResponseListeners().size();
        assertEquals(1, restultSize);
    }

    @Test
    public void testOnChildrenLoadedForSubscribedKey() {
        final String subscribedKey = "SubscribedKey";
        mediaIdSubscriptionCallback.registerMediaBrowserResponseListener(subscribedKey, mediaBrowserResponseListener);
        mediaIdSubscriptionCallback.onChildrenLoaded(subscribedKey, mediaItemList);
        shadowOf(Looper.getMainLooper()).idle();
        verify(mediaBrowserResponseListener, times(1)).onChildrenLoaded(eq(subscribedKey), captor.capture());
        ArrayList<MediaItem> children = captor.getValue();
        assertEquals(1, children.size());
        assertEquals(mockMediaItem, children.get(0));
    }

    @Test
    public void testOnChildrenLoadedForNonSubscribedKey() {
        final String subscribedKey = "SubscribedKey";
        final String nonSubscribedKey = "NonSubscribedKey";

        mediaIdSubscriptionCallback.registerMediaBrowserResponseListener(subscribedKey, mediaBrowserResponseListener);
        mediaIdSubscriptionCallback.onChildrenLoaded(nonSubscribedKey, mediaItemList);
        shadowOf(Looper.getMainLooper()).idle();
        verify(mediaBrowserResponseListener, never()).onChildrenLoaded(eq(nonSubscribedKey), any());
    }


}