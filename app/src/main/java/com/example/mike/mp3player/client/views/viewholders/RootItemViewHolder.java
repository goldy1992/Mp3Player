package com.example.mike.mp3player.client.views.viewholders;

import android.support.v4.media.MediaBrowserCompat;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;

import static com.example.mike.mp3player.commons.MediaItemUtils.getRootTitle;

public class RootItemViewHolder extends MediaItemViewHolder {
    private final TextView title;

    public RootItemViewHolder(@NonNull View itemView, AlbumArtPainter albumArtPainter) {
        super(itemView, albumArtPainter);
        this.title = itemView.findViewById(R.id.title);
    }

    @Override
    public void bindMediaItem(MediaBrowserCompat.MediaItem item) {
        title.setText(getRootTitle(item));
    }

}
