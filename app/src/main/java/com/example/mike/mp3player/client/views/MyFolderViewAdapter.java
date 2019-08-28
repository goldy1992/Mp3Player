package com.example.mike.mp3player.client.views;

import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;

import static com.example.mike.mp3player.commons.MediaItemUtils.getDescription;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;

public class MyFolderViewAdapter extends MyGenericRecycleViewAdapter {

    private final String LOG_TAG = "FOLDER_VIEW_ADAPTER";

    public MyFolderViewAdapter(AlbumArtPainter albumArtPainter) {
        super(albumArtPainter);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = super.onCreateViewHolder(parent, viewType);
        if (vh == null) {
            // create a new views
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.folder_item_menu, parent, false);

            vh = new MyFolderViewHolder(view);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final boolean isFolderHolder = holder instanceof MyFolderViewHolder;
        if (isFolderHolder && !isEmptyRecycleView()) {
            MyFolderViewHolder folderViewHolder = (MyFolderViewHolder) holder;
            //Log.i(LOG_TAG, "position: " + position);
            MediaBrowserCompat.MediaItem song = getItems().get(holder.getAdapterPosition());
            // - get element from your dataset at this position
            // - replace the contents of the views with that element
            String folderName = extractFolderName(song);
            folderViewHolder.folderName.setText(folderName);
            String folderPath = extractFolderPath(song);
            folderViewHolder.folderPath.setText(folderPath);
        }
    }

    private String extractFolderName(MediaBrowserCompat.MediaItem song) {
        return getTitle(song);
    }

    private String extractFolderPath(MediaBrowserCompat.MediaItem song) {
        return getDescription(song);
    }
}
