package com.github.goldy1992.mp3player.client.views.adapters;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.client.views.viewholders.EmptyListViewHolder;
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder;
import com.github.goldy1992.mp3player.client.views.viewholders.MyFolderViewHolder;
import com.github.goldy1992.mp3player.client.views.viewholders.MySongViewHolder;
import com.github.goldy1992.mp3player.client.views.viewholders.RootItemViewHolder;
import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;

import javax.inject.Inject;

import static com.github.goldy1992.mp3player.commons.Constants.MEDIA_ITEM_TYPE;

public class SearchResultAdapter extends MediaItemRecyclerViewAdapter {

    @Inject
    public SearchResultAdapter(AlbumArtPainter albumArtPainter, Handler mainHandler) {
        super(albumArtPainter, mainHandler);
    }

    @NonNull
    @Override
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = null;
        if (viewType == MediaItemType.SONG.getValue()) {
            // create a new views
            layoutInflater = LayoutInflater.from(parent.getContext());
            ViewGroup v = (ViewGroup) layoutInflater
                    .inflate(R.layout.song_item_menu, parent, false);
            return new MySongViewHolder(v, albumArtPainter);
        }
        else if (viewType == MediaItemType.ROOT.getValue()) {
            // create a new views
            layoutInflater = LayoutInflater.from(parent.getContext());
            ViewGroup v = (ViewGroup) layoutInflater
                    .inflate(R.layout.root_item_menu, parent, false);
            return new RootItemViewHolder(v, albumArtPainter);
        }
        else if (viewType == MediaItemType.FOLDER.getValue()) {
            layoutInflater = LayoutInflater.from(parent.getContext());
            ViewGroup v = (ViewGroup) layoutInflater
                    .inflate(R.layout.folder_item_menu, parent, false);
            return new MyFolderViewHolder(v, albumArtPainter);
        }
        else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.empty_item_menu, parent, false);
            return new EmptyListViewHolder(view, albumArtPainter);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MediaItemViewHolder holder, int position) {
        MediaBrowserCompat.MediaItem item = items.get(holder.getAdapterPosition());
        if (null != item) {
            holder.bindMediaItem(item);
        }

    }

    @Override
    public int getItemViewType(int position) {
        MediaBrowserCompat.MediaItem mediaItem = items.get(position);
        MediaItemType mediaItemType = (MediaItemType) MediaItemUtils.getExtra(MEDIA_ITEM_TYPE, mediaItem);
        if (null != mediaItemType) {
            return mediaItemType.getValue();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
