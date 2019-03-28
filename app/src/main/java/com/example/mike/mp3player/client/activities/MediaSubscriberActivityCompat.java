package com.example.mike.mp3player.client.activities;

import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;

import java.util.List;
import java.util.Map;

public abstract class MediaSubscriberActivityCompat extends MediaActivityCompat {

    private Map<MediaBrowserCompat.MediaItem, List<MediaBrowserCompat.MediaItem>> initialItems;
    private static final SubscriptionType subscriptionType;

    abstract Map<MediaBrowserCompat.MediaItem, List<MediaBrowserCompat.MediaItem>> initMenuItems(Bundle extras);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initialItems = initMenuItems(getIntent().getExtras());

    }
}
