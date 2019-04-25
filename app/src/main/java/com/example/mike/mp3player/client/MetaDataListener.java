package com.example.mike.mp3player.client;

import android.support.v4.media.MediaMetadataCompat;

import org.jetbrains.annotations.NotNull;

public interface MetaDataListener {
    void onMetadataChanged(@NotNull MediaMetadataCompat metadata);
}
