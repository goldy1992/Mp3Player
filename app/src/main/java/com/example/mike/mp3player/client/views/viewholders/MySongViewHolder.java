package com.example.mike.mp3player.client.views.viewholders;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;

import static com.example.mike.mp3player.commons.MediaItemUtils.extractArtist;
import static com.example.mike.mp3player.commons.MediaItemUtils.extractDuration;
import static com.example.mike.mp3player.commons.MediaItemUtils.extractTitle;
import static com.example.mike.mp3player.commons.MediaItemUtils.getAlbumArtUri;

public class MySongViewHolder extends MediaItemViewHolder {

    private final TextView title;
    private final TextView artist;
    private final TextView duration;
    private final ImageView albumArt;

    public MySongViewHolder(@NonNull View itemView, AlbumArtPainter albumArtPainter) {
        super(itemView, albumArtPainter);
        this.artist = itemView.findViewById(R.id.artist);
        this.title = itemView.findViewById(R.id.title);
        this.duration = itemView.findViewById(R.id.duration);
        this.albumArt = itemView.findViewById(R.id.song_item_album_art);
    }

    @Override
    public void bindMediaItem(MediaBrowserCompat.MediaItem item) {
        // - get element from your dataset at this position
        // - replace the contents of the views with that element
        String title = extractTitle(item);
        String artist = extractArtist(item);
        String duration = extractDuration(item);
        this.artist.setText(artist);
        this.title.setText(title);
        this.duration.setText(duration);
        Uri uri = getAlbumArtUri(item);
        albumArtPainter.paintOnView(albumArt, uri);
    }



}
