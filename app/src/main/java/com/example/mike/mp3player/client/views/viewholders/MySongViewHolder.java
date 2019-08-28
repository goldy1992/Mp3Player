package com.example.mike.mp3player.client.views.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;

public class MySongViewHolder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final TextView artist;
    private final TextView duration;
    private final ImageView albumArt;

    public MySongViewHolder(@NonNull View itemView) {
        super(itemView);
        this.artist = itemView.findViewById(R.id.artist);
        this.title = itemView.findViewById(R.id.title);
        this.duration = itemView.findViewById(R.id.duration);
        this.albumArt = itemView.findViewById(R.id.song_item_album_art);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getArtist() {
        return artist;
    }

    public TextView getDuration() {
        return duration;
    }

    public ImageView getAlbumArt() {
        return albumArt;
    }
}
