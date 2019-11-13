package com.github.goldy1992.mp3player.client.views.adapters;

import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.client.views.viewholders.MediaPlayerTrackViewHolder;

import java.util.List;

public class TrackViewAdapter extends RecyclerView.Adapter<MediaPlayerTrackViewHolder> {
    final Handler mainHandler;
    List<QueueItem> queue;
    final AlbumArtPainter albumArtPainter;


    public TrackViewAdapter(AlbumArtPainter albumArtPainter,
                            Handler mainHandler,
                            List<QueueItem> queue) {
        super();
        this.queue = queue;
        // TODO: if there is no queue make an empty view holder
        this.albumArtPainter = albumArtPainter;
        this.mainHandler = mainHandler;
    }

    @NonNull
    @Override
    public MediaPlayerTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new views
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater
                .inflate(R.layout.view_holder_media_player, parent, false);
        return new MediaPlayerTrackViewHolder(v, albumArtPainter);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaPlayerTrackViewHolder holder, int position) {
        QueueItem queueItem = queue.get(position);
        holder.bindMediaItem(queueItem);
    }

    @Override
    public int getItemCount() {
        if (null != queue) {
            return queue.size();
        }
        return -1;
    }

    /**
     *
     */
    public void setQueue(List<QueueItem> queue) {
        this.queue = queue;
        notifyDataSetChanged();
    }




}
