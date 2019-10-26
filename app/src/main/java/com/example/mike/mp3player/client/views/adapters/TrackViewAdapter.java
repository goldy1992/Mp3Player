package com.example.mike.mp3player.client.views.adapters;

import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.metadata.MetadataListener;
import com.example.mike.mp3player.client.views.viewholders.MediaItemViewHolder;
import com.example.mike.mp3player.client.views.viewholders.MediaPlayerTrackViewHolder;

import java.util.List;

public class TrackViewAdapter extends RecyclerView.Adapter<MediaPlayerTrackViewHolder> implements MetadataListener {
    final Handler mainHandler;
    List<MediaSessionCompat.QueueItem> queue;
    final AlbumArtPainter albumArtPainter;
    final MediaControllerAdapter mediaControllerAdapter;

    public TrackViewAdapter(MediaControllerAdapter mediaControllerAdapter,
                            AlbumArtPainter albumArtPainter,
                            Handler mainHandler) {
        super();
        this.queue = mediaControllerAdapter.getQueue();
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.albumArtPainter = albumArtPainter;
        this.mainHandler = mainHandler;
    }

    @NonNull
    @Override
    public MediaPlayerTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MediaItemViewHolder vh = null;
        // create a new views
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater
                .inflate(R.layout.view_holder_media_player, parent, false);
        return new MediaPlayerTrackViewHolder(v, albumArtPainter);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaPlayerTrackViewHolder holder, int position) {
        MediaSessionCompat.QueueItem queueItem = queue.get(position);
        holder.bindMediaItem(queueItem);
    }


    @Override
    public int getItemCount() {
        return queue.size();
    }

    @Override
    public void onMetadataChanged(@NonNull MediaMetadataCompat metadata) {
        List<MediaSessionCompat.QueueItem> queueItems = mediaControllerAdapter.getQueue();
        this.queue = queueItems;
        this.notifyDataSetChanged();
    }
}
