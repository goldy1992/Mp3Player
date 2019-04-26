package com.example.mike.mp3player.client;

import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.NonNull;

public interface MetaDataListener {
    void onMetadataChanged(@NonNull MediaMetadataCompat metadata);
}
