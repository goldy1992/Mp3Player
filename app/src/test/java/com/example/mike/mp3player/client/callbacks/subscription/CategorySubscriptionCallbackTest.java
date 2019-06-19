package com.example.mike.mp3player.client.callbacks.subscription;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Test class for the CategorySubscriptionCallback
 */
class CategorySubscriptionCallbackTest {
    /** CategorySubscriptionCallback object to test */
    private CategorySubscriptionCallback categorySubscriptionCallback;
    /** MediaBrowserAdapter */
    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;
    /** setup method */
    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        categorySubscriptionCallback = new CategorySubscriptionCallback(mediaBrowserAdapter);
    }
    /**
     * verifies the SubscriptionType for this class is SubscriptionType.CATEGORY
     */
    @Test
    void testGetType() {
        assertEquals(SubscriptionType.CATEGORY, categorySubscriptionCallback.getType());
    }
    /**
     * test onChildrenLoaded method
     * @exception IllegalAccessException illegal access exception
     * GIVEN: a categorySubscriptionCallback with a listener L.
     * WHEN: onChildrenLoaded is called
     * THEN: then L is called once
     */
    @Test
    @SuppressWarnings("unchecked")
    void testOnChildrenLoaded() throws IllegalAccessException {
        // initialisation
        final String parentId = "dummy";
        final Category category = Category.ARTISTS;
        final MediaBrowserCompat.MediaItem firstMockMediaItem = mock(MediaBrowserCompat.MediaItem.class);
        final MediaBrowserCompat.MediaItem secondMockMediaItem = mock(MediaBrowserCompat.MediaItem.class);
        final List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        mediaItems.add(firstMockMediaItem);
        mediaItems.add(secondMockMediaItem);
        final Bundle options = mock(Bundle.class);
        final LibraryRequest libraryRequest = new LibraryRequest(category, parentId);
        when(options.get(REQUEST_OBJECT)).thenReturn(libraryRequest);
        MediaBrowserResponseListenerTestImpl listener = new MediaBrowserResponseListenerTestImpl();
        MediaBrowserResponseListenerTestImpl spiedListener = spy(listener);
        final Set<MediaBrowserResponseListener> listenerSet = new HashSet<>();
        listenerSet.add(spiedListener);
        Map<Category, Set<MediaBrowserResponseListener>> listenerMap =
                (Map<Category, Set<MediaBrowserResponseListener>>)
                        FieldUtils.readField(categorySubscriptionCallback,
                                "mediaBrowserResponseListeners", true);
        listenerMap.put(category, listenerSet);
        // call on children loaded
        categorySubscriptionCallback.onChildrenLoaded(parentId, mediaItems, options);
        // verify it is called once
        Mockito.verify(spiedListener, times(1)).onChildrenLoaded(any(), any(), any(), any());
    }

    /**
     * Test Implementation for the purpose of testing
     * TODO: consider moving to separate isolated class if needed for future tests
     */
    private static class MediaBrowserResponseListenerTestImpl implements MediaBrowserResponseListener {

        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options, Context context) {

        }
    }

}