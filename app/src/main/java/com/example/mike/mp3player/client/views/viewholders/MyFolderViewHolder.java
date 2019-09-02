package com.example.mike.mp3player.client.views.viewholders;

import android.support.v4.media.MediaBrowserCompat;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;

import static com.example.mike.mp3player.commons.MediaItemUtils.extractFolderName;
import static com.example.mike.mp3player.commons.MediaItemUtils.extractFolderPath;

public class MyFolderViewHolder extends MediaItemViewHolder {

    private final TextView folderName;
    private final TextView folderPath;

    public MyFolderViewHolder(@NonNull View itemView, AlbumArtPainter albumArtPainter) {
        super(itemView, albumArtPainter);
        this.folderName = itemView.findViewById(R.id.folderName);
        this.folderPath = itemView.findViewById(R.id.folderPath);
    }

    @Override
    public void bindMediaItem(MediaBrowserCompat.MediaItem item) {
        String folderNameText = extractFolderName(item);
        folderName.setText(folderNameText);
        String folderPathText = extractFolderPath(item);
        folderPath.setText(folderPathText);
    }

}
