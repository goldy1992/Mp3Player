package com.example.mike.mp3player.client.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;

public class MySongViewHolder extends RecyclerView.ViewHolder {

    final TextView titleTextView;
    final TextView titleArtistView;
    final TextView titleDurationView;
    final ImageView albumArt;

    public MySongViewHolder(@NonNull View itemView) {
        super(itemView);
        this.titleArtistView = itemView.findViewById(R.id.artist);
        this.titleTextView = itemView.findViewById(R.id.title);
        this.titleDurationView = itemView.findViewById(R.id.duration);
        this.albumArt = itemView.findViewById(R.id.song_item_album_art);
    }
}
