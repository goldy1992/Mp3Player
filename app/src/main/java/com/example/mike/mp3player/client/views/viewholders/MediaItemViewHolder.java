package com.example.mike.mp3player.client.views.viewholders;

import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.client.AlbumArtPainter;

public abstract class MediaItemViewHolder extends RecyclerView.ViewHolder {

    protected final AlbumArtPainter albumArtPainter;

    public MediaItemViewHolder(@NonNull View itemView, AlbumArtPainter albumArtPainter) {
        super(itemView);
        this.albumArtPainter = albumArtPainter;
    }

    public abstract void bindMediaItem(MediaItem item);
}
