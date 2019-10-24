package com.example.mike.mp3player.client.views.adapters;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.RequestBuilder;
import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.views.viewholders.MediaItemViewHolder;
import com.example.mike.mp3player.client.views.viewholders.MyFolderViewHolder;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_DIRECTORY;

public class MyFolderViewAdapter extends MyGenericRecycleViewAdapter {

    private final String LOG_TAG = "FOLDER_VIEW_ADAPTER";

    public MyFolderViewAdapter(AlbumArtPainter albumArtPainter, Handler mainHandler) {
        super(albumArtPainter, mainHandler);
    }

    @Override
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MediaItemViewHolder vh = super.onCreateViewHolder(parent, viewType);
        if (vh == null) {
            // create a new views
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.folder_item_menu, parent, false);

            vh = new MyFolderViewHolder(view, albumArtPainter);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(MediaItemViewHolder holder, int position) {
        final boolean isFolderHolder = holder instanceof MyFolderViewHolder;
        if (isFolderHolder && !isEmptyRecycleView()) {
            MyFolderViewHolder folderViewHolder = (MyFolderViewHolder) holder;
            //Log.i(LOG_TAG, "position: " + position);
            MediaBrowserCompat.MediaItem song = getItems().get(holder.getAdapterPosition());
            folderViewHolder.bindMediaItem(song);
        }
    }

    @NonNull
    @Override
    public String getSectionText(int position) {
        Bundle extras = items.get(position).getDescription().getExtras();

        if (null != extras) {
            File directory = (File) extras.getSerializable(META_DATA_DIRECTORY);
            if (null != directory) {
                return directory.getName().substring(0, 1);
            }
        }
        return UNKNOWN;
    }

    @NonNull
    @Override
    public List<MediaBrowserCompat.MediaItem> getPreloadItems(int position) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull MediaBrowserCompat.MediaItem item) {
        return null;
    }
}
