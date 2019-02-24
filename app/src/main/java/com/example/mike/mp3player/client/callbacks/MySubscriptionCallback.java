package com.example.mike.mp3player.client.callbacks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback;

import com.example.mike.mp3player.client.MediaBrowserResponseListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;

public class MySubscriptionCallback extends SubscriptionCallback {

    private static final String LOG_TAG = "SUBSCRIPTION_CALLBACK";
    private Set<MediaBrowserResponseListener> mediaBrowserResponseListeners;
    private Context context;

    public MySubscriptionCallback(Context context) {
        super();
        this.context = context;
        mediaBrowserResponseListeners = new HashSet<>();
    }
    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
        onChildrenLoaded(parentId, children, null);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children,
                                 @NonNull Bundle options) {
        super.onChildrenLoaded(parentId, children, options);

        // TODO: maybe implement logic to decide which listener the response should be sent to.
        ArrayList<MediaBrowserCompat.MediaItem> childrenArrayList = new ArrayList<>(children);
        for(MediaBrowserResponseListener listener : mediaBrowserResponseListeners) {
            listener.onChildrenLoaded(parentId, childrenArrayList, options, context);
        }
    }

    public synchronized void registerMediaBrowserResponseListener(MediaBrowserResponseListener listener) {
        mediaBrowserResponseListeners.add(listener);
    }

    public synchronized void registerMediaBrowserResponseListeners(Collection<MediaBrowserResponseListener> listeners) {
        mediaBrowserResponseListeners.addAll(listeners);
    }

    public synchronized boolean removeMediaBrowserResponseListener(MediaBrowserResponseListener listener) {
        return mediaBrowserResponseListeners.remove(listener);
    }
}
