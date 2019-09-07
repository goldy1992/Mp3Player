package com.example.mike.mp3player.client.views.adapters;

import android.support.v4.media.MediaBrowserCompat;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.views.viewholders.MediaItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class MediaItemRecyclerViewAdapter extends RecyclerView.Adapter<MediaItemViewHolder> {

    final AlbumArtPainter albumArtPainter;
    List<MediaBrowserCompat.MediaItem> items = new ArrayList<>();

    public MediaItemRecyclerViewAdapter(AlbumArtPainter albumArtPainter) {
        super();
        this.albumArtPainter = albumArtPainter;
    }

    public List<MediaBrowserCompat.MediaItem> getItems() {
        return items;
    }
}
