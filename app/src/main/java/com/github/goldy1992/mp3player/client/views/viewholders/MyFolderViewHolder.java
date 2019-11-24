package com.github.goldy1992.mp3player.client.views.viewholders;

import android.support.v4.media.MediaBrowserCompat;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.AlbumArtPainter;

import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryName;
import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath;

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

    private String extractFolderName(MediaBrowserCompat.MediaItem song) {
        return getDirectoryName(song);
    }

    private String extractFolderPath(MediaBrowserCompat.MediaItem song) {
        return getDirectoryPath(song);
    }
}
