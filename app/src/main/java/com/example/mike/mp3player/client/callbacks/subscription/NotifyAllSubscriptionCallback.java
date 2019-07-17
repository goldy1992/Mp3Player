package com.example.mike.mp3player.client.callbacks.subscription;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.client.MediaBrowserResponseListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotifyAllSubscriptionCallback extends GenericSubscriptionCallback<Object> {

    private static final String LOG_TAG = "NTFY_ALL_SBRPT_CLBK";
    private final Object SINGLETON_KEY = new Object();
    private final Set<MediaBrowserResponseListener> SUBSCRIBER_MAP = new HashSet<>();

    @Override
    public SubscriptionType getType() {
        return SubscriptionType.NOTIFY_ALL;
    }

    public NotifyAllSubscriptionCallback(Handler handler) {
        super(handler);

        this.mediaBrowserResponseListeners = Collections.singletonMap(SINGLETON_KEY, SUBSCRIBER_MAP);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children,
                                 @NonNull Bundle options) {
        ArrayList<MediaBrowserCompat.MediaItem> childrenArrayList = new ArrayList<>(children);
        for (MediaBrowserResponseListener m : SUBSCRIBER_MAP) {
            m.onChildrenLoaded(parentId, childrenArrayList, options);
        }
    }

    @Override
    public synchronized void registerMediaBrowserResponseListener(@Nullable Object key , MediaBrowserResponseListener listener) {
        SUBSCRIBER_MAP.add(listener);
    }

    public synchronized void registerMediaBrowserResponseListeners(@Nullable Object key, Collection<MediaBrowserResponseListener> listeners) {
        SUBSCRIBER_MAP.addAll(listeners);
    }

    @Override
    public synchronized boolean removeMediaBrowserResponseListener(@Nullable Object key, MediaBrowserResponseListener listener) {
        return SUBSCRIBER_MAP.remove(listener);
    }
}
