package com.example.mike.mp3player.client.activities;

import android.os.Bundle;

public abstract class MediaBrowserSubscriberActivityCompat extends MediaBrowserCreatorActivityCompat {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMediaBrowserService();
    }
}
