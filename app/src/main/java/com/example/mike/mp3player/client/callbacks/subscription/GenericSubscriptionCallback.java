package com.example.mike.mp3player.client.callbacks.subscription;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GenericSubscriptionCallback<K> extends MediaBrowserCompat.SubscriptionCallback {
    public abstract SubscriptionType getType();
    private static final String LOG_TAG = "SUBSCRIPTION_CALLBACK";
    Handler handler;
    Map<K, Set<MediaBrowserResponseListener>> mediaBrowserResponseListeners;
    Context context;

    public GenericSubscriptionCallback(Handler handler) {
        super();
        this.context = context;
        this.mediaBrowserResponseListeners = new HashMap<>();
        this.handler = handler;

    }

    @Override
    public abstract void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children,
                                 @NonNull Bundle options);

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
