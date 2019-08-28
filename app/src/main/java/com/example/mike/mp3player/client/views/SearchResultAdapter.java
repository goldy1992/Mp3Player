package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.views.viewholders.MyFolderViewHolder;
import com.example.mike.mp3player.client.views.viewholders.MySongViewHolder;
import com.example.mike.mp3player.client.views.viewholders.RootItemViewHolder;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.MediaItemUtils.extractArtist;
import static com.example.mike.mp3player.commons.MediaItemUtils.extractDuration;
import static com.example.mike.mp3player.commons.MediaItemUtils.extractFolderName;
import static com.example.mike.mp3player.commons.MediaItemUtils.extractFolderPath;
import static com.example.mike.mp3player.commons.MediaItemUtils.extractTitle;
import static com.example.mike.mp3player.commons.MediaItemUtils.getAlbumArtUri;
import static com.example.mike.mp3player.commons.MediaItemUtils.getRootTitle;

public class SearchResultAdapter extends RecyclerView.Adapter {

    public List<MediaBrowserCompat.MediaItem> items = new ArrayList<>();
    private AlbumArtPainter albumArtPainter;
    public SearchResultAdapter(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.albumArtPainter = new AlbumArtPainter(context, handler);
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
            return new MySongViewHolder(v);
        }
        else if (viewType == MediaItemType.ROOT.getValue()) {
            // create a new views
            layoutInflater = LayoutInflater.from(parent.getContext());
            ViewGroup v = (ViewGroup) layoutInflater
                    .inflate(R.layout.root_item_menu, parent, false);
            return new RootItemViewHolder(v);
        }
        else if (viewType == MediaItemType.FOLDER.getValue()) {
            layoutInflater = LayoutInflater.from(parent.getContext());
            ViewGroup v = (ViewGroup) layoutInflater
                    .inflate(R.layout.folder_item_menu, parent, false);
            return new MyFolderViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MediaBrowserCompat.MediaItem item = items.get(holder.getAdapterPosition());
        Bundle extras = item.getDescription().getExtras();
        MediaItemType mediaItemType = (MediaItemType) extras.get(MEDIA_ITEM_TYPE);
        if (null != mediaItemType) {
            switch (mediaItemType) {
                case SONG:
                    MySongViewHolder mySongViewHolder = (MySongViewHolder) holder;
                    // - get element from your dataset at this position
                    // - replace the contents of the views with that element
                    String title = extractTitle(item);
                    String artist = extractArtist(item);
                    String duration = extractDuration(item);

                    mySongViewHolder.getArtist().setText(artist);
                    mySongViewHolder.getTitle().setText(title);
                    mySongViewHolder.getDuration().setText(duration);
                    ImageView albumArt = mySongViewHolder.getAlbumArt();
                    Uri uri = getAlbumArtUri(item);
                    albumArtPainter.paintOnView(albumArt, uri);
                    break;
                case FOLDER:
                    MyFolderViewHolder folderViewHolder = (MyFolderViewHolder) holder;
                    //Log.i(LOG_TAG, "position: " + position);
                    MediaBrowserCompat.MediaItem song = items.get(folderViewHolder.getAdapterPosition());
                    // - get element from your dataset at this position
                    // - replace the contents of the views with that element
                    String folderName = extractFolderName(song);
                    folderViewHolder.getFolderName().setText(folderName);
                    String folderPath = extractFolderPath(song);
                    folderViewHolder.getFolderPath().setText(folderPath);
                    break;
                case ROOT:
                    RootItemViewHolder rootItemViewHolder = (RootItemViewHolder) holder;
                    song = items.get(rootItemViewHolder.getAdapterPosition());
                    rootItemViewHolder.getTitle().setText(getRootTitle(song));
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
