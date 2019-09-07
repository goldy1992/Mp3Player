package com.example.mike.mp3player.client.callbacks.subscription;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.MediaBrowserResponseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MediaIdSubscriptionCallback extends GenericSubscriptionCallback {

    public MediaIdSubscriptionCallback(Handler handler) {
        super(handler);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {

    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
        ArrayList<MediaBrowserCompat.MediaItem> childrenArrayList = new ArrayList<>(children);
        Set<MediaBrowserResponseListener> listenersToNotify = mediaBrowserResponseListeners.get(parentId);
        if (null != listenersToNotify) {
            for (MediaBrowserResponseListener listener : listenersToNotify) {
                listener.onChildrenLoaded(parentId, childrenArrayList);
            }
        }
    }

    @Override
    public SubscriptionType getType() {
        return SubscriptionType.MEDIA_ID;
    }
}
