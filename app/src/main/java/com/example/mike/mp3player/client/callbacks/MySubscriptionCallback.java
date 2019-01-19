package com.example.mike.mp3player.client.callbacks;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback;

import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;

import java.util.List;

public class MySubscriptionCallback extends SubscriptionCallback {

    private MediaBrowserConnectorCallback mediaBrowserConnectorCallback;
    private static final String LOG_TAG = "SUBSCRIPTION_CALLBACK";
    public MySubscriptionCallback(MediaBrowserConnectorCallback mediaBrowserConnectorCallback) {
        super();
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
    }
    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
        onChildrenLoaded(parentId, children, null);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children,
                                 @NonNull Bundle options) {
        super.onChildrenLoaded(parentId, children, options);
        mediaBrowserConnectorCallback.onChildrenLoaded(parentId, children, options);
    }
}
