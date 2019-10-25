package com.example.mike.mp3player.client.views.viewholders;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;

import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST;

public class MediaPlayerTrackViewHolder extends RecyclerView.ViewHolder {

    private AlbumArtPainter albumArtPainter;
    private final TextView title;
    private final TextView artist;
    private final ImageView albumArt;

    public MediaPlayerTrackViewHolder(@NonNull View itemView, AlbumArtPainter albumArtPainter) {
        super(itemView);
        this.albumArtPainter = albumArtPainter;
        this.title = itemView.findViewById(R.id.songTitle);
        this.artist = itemView.findViewById(R.id.songArtist);
        this.albumArt = itemView.findViewById(R.id.albumArt);
    }

    public void bindMediaItem(MediaSessionCompat.QueueItem item) {
        final String titleText = item.getDescription().getTitle().toString();
        this.title.setText(titleText);

        final Bundle extras = item.getDescription().getExtras();
        final String artistText = extras.getString(METADATA_KEY_ARTIST);
        this.artist.setText(artistText);

        final Uri albumArtUri = (Uri) extras.get(METADATA_KEY_ALBUM_ART_URI);
        this.albumArtPainter.paintOnView(albumArt, albumArtUri);
    }
}
