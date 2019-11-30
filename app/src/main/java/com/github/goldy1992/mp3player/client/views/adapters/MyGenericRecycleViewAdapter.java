package com.github.goldy1992.mp3player.client.views.adapters;

import android.os.Handler;
import android.support.v4.media.MediaDescriptionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.bumptech.glide.ListPreloader;
import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener;
import com.github.goldy1992.mp3player.client.views.viewholders.EmptyListViewHolder;
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder;
import com.l4digital.fastscroll.FastScroller;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.github.goldy1992.mp3player.commons.Constants.FIRST;

public abstract class MyGenericRecycleViewAdapter extends MediaItemRecyclerViewAdapter implements
        MediaBrowserResponseListener, FastScroller.SectionIndexer, ListPreloader.PreloadModelProvider<MediaItem> {
    final String LOG_TAG = "MY_VIEW_ADAPTER";
    private static final String EMPTY_MEDIA_ID = "EMPTY_MEDIA_ID";
    final int EMPTY_VIEW_TYPE = -1;



    private boolean isInitialised = false;
    private final MediaItem EMPTY_LIST_ITEM = buildEmptyListMediaItem();

    public MyGenericRecycleViewAdapter(AlbumArtPainter albumArtPainter, Handler mainHandler) {
        super(albumArtPainter, mainHandler);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0: items.size();
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaItem> children) {


        if (!children.isEmpty()) {
            this.items = children;
            mainHandler.post(this::notifyDataSetChanged);
        } else {
            addNoChildrenFoundItem();
        }
        this.isInitialised = true;
    }
    @Override
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW_TYPE) {
            View view =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.empty_item_menu, parent, false);
            return new EmptyListViewHolder(view, albumArtPainter);
        }
        return null;
    }

    @Override
    public int getItemViewType (int position) {
        if (position == FIRST && isEmptyRecycleView()) {
            return EMPTY_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    private void addNoChildrenFoundItem() {
        items.add(EMPTY_LIST_ITEM);
        mainHandler.post(this::notifyDataSetChanged);
    }

    private MediaItem buildEmptyListMediaItem() {
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setMediaId(EMPTY_MEDIA_ID)
                .build();
        return new MediaItem(mediaDescriptionCompat, MediaItem.FLAG_PLAYABLE);
    }

    protected boolean isEmptyRecycleView() {
        return getItems().isEmpty() || getItems().get(FIRST).equals(EMPTY_LIST_ITEM);
    }

    @VisibleForTesting
    public void setItems(List<MediaItem> items) {
        this.items = items;
    }
}
