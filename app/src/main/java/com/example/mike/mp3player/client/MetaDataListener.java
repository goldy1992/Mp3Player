package com.example.mike.mp3player.client;

import android.support.v4.media.MediaMetadataCompat;

public interface MetaDataListener {
    void onMetadataChanged(MediaMetadataCompat metadata);
}
