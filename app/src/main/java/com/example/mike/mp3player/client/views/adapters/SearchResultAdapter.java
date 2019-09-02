package com.example.mike.mp3player.client.views.adapters;

import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.views.viewholders.MyFolderViewHolder;
import com.example.mike.mp3player.client.views.viewholders.MySongViewHolder;
import com.example.mike.mp3player.client.views.viewholders.RootItemViewHolder;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;

public class SearchResultAdapter extends MediaItemRecyclerViewAdapter {

    @Inject
    public SearchResultAdapter(AlbumArtPainter albumArtPainter) {
        super(albumArtPainter);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MediaBrowserCompat.MediaItem item = items.get(holder.getAdapterPosition());
        if (null == item) {
            return;
        }
        MediaItemType mediaItemType = MediaItemUtils.getMediaItemType(item);
        if (null != mediaItemType) {
            switch (mediaItemType) {
                case SONG:
                    MySongViewHolder mySongViewHolder = (MySongViewHolder) holder;
                    mySongViewHolder.bindMediaItem(item);
                    break;
                case FOLDER:
                    MyFolderViewHolder folderViewHolder = (MyFolderViewHolder) holder;
                    folderViewHolder.bindMediaItem(item);
                    break;
                case ROOT:
                    RootItemViewHolder rootItemViewHolder = (RootItemViewHolder) holder;
                    rootItemViewHolder.bindMediaItem(item);
                    break;
            } // switch
        } // if MediaItemType not null
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
