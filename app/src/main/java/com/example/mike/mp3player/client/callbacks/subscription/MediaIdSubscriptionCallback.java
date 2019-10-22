package com.example.mike.mp3player.client.callbacks.subscription;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.MediaBrowserResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MediaIdSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback {

    private static final String LOG_TAG = "SUBSCRIPTION_CALLBACK";
    private Handler handler;
    private final Map<String, Set<MediaBrowserResponseListener>> mediaBrowserResponseListeners;

    public MediaIdSubscriptionCallback(Handler handler) {
        this.mediaBrowserResponseListeners = new HashMap<>();
        this.handler = handler;

    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {

    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
        handler.post( () -> {
            ArrayList<MediaBrowserCompat.MediaItem> childrenArrayList = new ArrayList<>(children);
            Set<MediaBrowserResponseListener> listenersToNotify = mediaBrowserResponseListeners.get(parentId);
            if (null != listenersToNotify) {
                for (MediaBrowserResponseListener listener : listenersToNotify) {
                    listener.onChildrenLoaded(parentId, childrenArrayList);
                }
            }
        });
    }

    public synchronized void registerMediaBrowserResponseListener(String key , MediaBrowserResponseListener listener) {
        if (mediaBrowserResponseListeners.get(key) == null) {
            mediaBrowserResponseListeners.put(key, new HashSet<>());
        }
        mediaBrowserResponseListeners.get(key).add(listener);
    }

}
