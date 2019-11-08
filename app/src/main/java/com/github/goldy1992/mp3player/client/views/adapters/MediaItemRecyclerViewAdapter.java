package com.github.goldy1992.mp3player.client.views.adapters;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import androidx.recyclerview.widget.RecyclerView;

import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class MediaItemRecyclerViewAdapter extends RecyclerView.Adapter<MediaItemViewHolder> {

    final AlbumArtPainter albumArtPainter;
    List<MediaBrowserCompat.MediaItem> items = new ArrayList<>();
    final Handler mainHandler;

    public MediaItemRecyclerViewAdapter(AlbumArtPainter albumArtPainter, Handler handler) {
        super();
        this.albumArtPainter = albumArtPainter;
        this.mainHandler = handler;
    }

    public List<MediaBrowserCompat.MediaItem> getItems() {
        return items;
    }
}
