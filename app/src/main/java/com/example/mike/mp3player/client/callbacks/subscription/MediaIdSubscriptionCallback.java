package com.example.mike.mp3player.client.callbacks.subscription;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;

public class MediaIdSubscriptionCallback extends GenericSubscriptionCallback<String> {

    public MediaIdSubscriptionCallback(MediaBrowserAdapter mediaBrowserAdapter) {
        super(mediaBrowserAdapter);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {
        ArrayList<MediaBrowserCompat.MediaItem> childrenArrayList = new ArrayList<>(children);
        Set<MediaBrowserResponseListener> listenersToNotify = mediaBrowserResponseListeners.get(parentId);
        if (null != listenersToNotify) {
            for (MediaBrowserResponseListener listener : listenersToNotify) {
                listener.onChildrenLoaded(parentId, childrenArrayList, options, context);
            }
        }
    }

    @Override
    public SubscriptionType getType() {
        return SubscriptionType.MEDIA_ID;
    }
}
