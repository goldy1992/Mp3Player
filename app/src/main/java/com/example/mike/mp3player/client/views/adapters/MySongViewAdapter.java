package com.example.mike.mp3player.client.views.adapters;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.views.viewholders.MySongViewHolder;



public class MySongViewAdapter extends MyGenericRecycleViewAdapter {
    private final String LOG_TAG = "MY_VIEW_ADAPTER";

    public MySongViewAdapter(AlbumArtPainter albumArtPainter) {
        super(albumArtPainter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = super.onCreateViewHolder(parent, viewType);
        if (vh == null) {
            // create a new views
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View v = layoutInflater
                    .inflate(R.layout.song_item_menu, parent, false);
            vh = new MySongViewHolder(v, albumArtPainter);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final boolean isSongHolder = holder instanceof MySongViewHolder;
        if (isSongHolder && !isEmptyRecycleView()) {
            MySongViewHolder songViewHolder = (MySongViewHolder) holder;
            // TODO: look into the use of holder.getAdapterPosition rather than the position parameter.
            //Log.i(LOG_TAG, "position: " + position);
            MediaItem song = items.get(holder.getAdapterPosition());
            songViewHolder.bindMediaItem(song);
        }
    }

}

