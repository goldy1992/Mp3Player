package com.example.mike.mp3player.client.views;

import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.Constants.FIRST;

public abstract class MyGenericRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> implements
        MediaBrowserResponseListener, FastScrollRecyclerView.SectionedAdapter {
    final String LOG_TAG = "MY_VIEW_ADAPTER";
    private static final String EMPTY_MEDIA_ID = "EMPTY_MEDIA_ID";
    final int EMPTY_VIEW_TYPE = -1;
    final AlbumArtPainter albumArtPainter;

    private List<MediaItem> items = new ArrayList<>();
    private boolean isInitialised = false;
    private final MediaItem EMPTY_LIST_ITEM = buildEmptyListMediaItem();

    public MyGenericRecycleViewAdapter(AlbumArtPainter albumArtPainter) {
        super();
        this.albumArtPainter = albumArtPainter;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0: items.size();
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaItem> children) {
        if (!isInitialised && children.isEmpty()) {
            addNoChildrenFoundItem();
        }

        if (!children.isEmpty()) {
            this.items.addAll(children);
            this.getItems().addAll(children);
            notifyDataSetChanged();
        }
        this.isInitialised = true;
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW_TYPE) {
            GridLayout t = (GridLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.empty_item_menu, parent, false);
            return new MyViewHolder(t);
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
        notifyDataSetChanged();
    }

    private MediaItem buildEmptyListMediaItem() {
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setMediaId(EMPTY_MEDIA_ID)
                .build();
        return new MediaItem(mediaDescriptionCompat, MediaItem.FLAG_PLAYABLE);
    }

    public List<MediaItem> getItems() {
        return items;
    }

    protected boolean isEmptyRecycleView() {
        return getItems().isEmpty() || getItems().get(FIRST).equals(EMPTY_LIST_ITEM);
    }

    @VisibleForTesting
    public void setItems(List<MediaItem> items) {
        this.items = items;
    }

    @Override
    public String getSectionName(int position) {
        return items.get(position).getDescription().getTitle().toString().substring(0, 1);
    }

}
