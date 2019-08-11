package com.example.mike.mp3player.client;

import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public interface MediaBrowserResponseListener {

    void onChildrenLoaded(@NonNull String parentId,
                          @NonNull ArrayList<MediaBrowserCompat.MediaItem> children);

}
