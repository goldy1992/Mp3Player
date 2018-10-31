package com.example.mike.mp3player.client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback;
import android.util.Log;

import java.util.List;

public class MySubscriptionCallback extends SubscriptionCallback {

    private MediaPlayerActivity mediaPlayerActivity;
    private static final String LOG_TAG = "MY_SUBSCRIPTION_CALLBACK";
    public MySubscriptionCallback(MediaPlayerActivity mediaPlayerActivity) {
        super();
        this.mediaPlayerActivity = mediaPlayerActivity;
    }
    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
        onChildrenLoaded(parentId, children, null);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children,
                                 @NonNull Bundle options) {
        super.onChildrenLoaded(parentId, children, options);
        Log.d(LOG_TAG, children.toString());
    }
}
