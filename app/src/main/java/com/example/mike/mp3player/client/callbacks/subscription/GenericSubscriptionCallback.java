package com.example.mike.mp3player.client.callbacks.subscription;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.commons.Range;
import com.example.mike.mp3player.commons.library.LibraryId;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;

import static com.example.mike.mp3player.commons.Constants.PARENT_ID;
import static com.example.mike.mp3player.commons.Constants.RANGE_SIZE;

public abstract class GenericSubscriptionCallback<K> extends MediaBrowserCompat.SubscriptionCallback {
    public abstract SubscriptionType getType();
    private static final String LOG_TAG = "SUBSCRIPTION_CALLBACK";
    Handler handler;
    Map<K, Set<MediaBrowserResponseListener>> mediaBrowserResponseListeners;
    Context context;
    MediaBrowserAdapter mediaBrowserAdapter;

    public GenericSubscriptionCallback(MediaBrowserAdapter mediaBrowserAdapter) {
        super();
        this.context = mediaBrowserAdapter.getContext();
        this.mediaBrowserResponseListeners = new HashMap<>();
        this.handler = new Handler(mediaBrowserAdapter.getLooper());
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
        onChildrenLoaded(parentId, children, null);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children,
                                 @NonNull Bundle options) {
        super.onChildrenLoaded(parentId, children, options);

        Range range = MediaLibraryUtils.parseRangeFromBundleExtras(options);
        if (range != null && children != null && children.size() >= RANGE_SIZE) {
            LibraryId libraryId = (LibraryId) options.get(PARENT_ID);
            int newLower = range.getUpper()+1;
            int newUpper = newLower + RANGE_SIZE;
            mediaBrowserAdapter.subscribe(libraryId, new Range(newLower, newUpper));
        }
    }

    public synchronized void registerMediaBrowserResponseListener(K key , MediaBrowserResponseListener listener) {
        if (mediaBrowserResponseListeners.get(key) == null) {
            mediaBrowserResponseListeners.put(key, new HashSet<>());
        }
        mediaBrowserResponseListeners.get(key).add(listener);
    }

    public synchronized void registerMediaBrowserResponseListeners(K key, Collection<MediaBrowserResponseListener> listeners) {
        if (mediaBrowserResponseListeners.get(key) == null) {
            mediaBrowserResponseListeners.put(key, new HashSet<>());
        }
        mediaBrowserResponseListeners.get(key).addAll(listeners);
    }

    public synchronized boolean removeMediaBrowserResponseListener(K key, MediaBrowserResponseListener listener) {
        return mediaBrowserResponseListeners.get(key).remove(listener);
    }

}
