package com.example.mike.mp3player.client.callbacks;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback;
import android.util.Log;

import com.example.mike.mp3player.client.MainActivity;

import java.util.List;

public class MySubscriptionCallback extends SubscriptionCallback {

    private MainActivity mainActivity;
    private static final String LOG_TAG = "SUBSCRIPTION_CALLBACK";
    public MySubscriptionCallback(MainActivity mainActivity) {
        super();
        this.mainActivity = mainActivity;
    }
    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
        onChildrenLoaded(parentId, children, null);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children,
                                 @NonNull Bundle options) {
        super.onChildrenLoaded(parentId, children, options);
        mainActivity.init(children);
        Log.d(LOG_TAG, children.toString());
    }
}
