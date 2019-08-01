package com.example.mike.mp3player.client.callbacks.metadata;

import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.NonNull;

public interface MetadataListener {
    void onMetadataChanged(@NonNull MediaMetadataCompat metadata);
}
