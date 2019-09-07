package com.example.mike.mp3player.client.views.viewholders;

import android.support.v4.media.MediaBrowserCompat;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.AlbumArtPainter;

public class EmptyListViewHolder extends MediaItemViewHolder {
    public EmptyListViewHolder(@NonNull View itemView, AlbumArtPainter albumArtPainter) {
        super(itemView, albumArtPainter);
    }

    @Override
    public void bindMediaItem(MediaBrowserCompat.MediaItem item) {

    }
}
