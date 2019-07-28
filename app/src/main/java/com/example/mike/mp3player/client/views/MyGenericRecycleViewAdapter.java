package com.example.mike.mp3player.client.views;

import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.commons.library.Category;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.Constants.FIRST;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;
import static com.example.mike.mp3player.commons.MediaItemUtils.hasTitle;

public abstract class MyGenericRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> implements
        Filterable, MediaBrowserResponseListener, FastScrollRecyclerView.SectionedAdapter {
    final String LOG_TAG = "MY_VIEW_ADAPTER";
    private static final String EMPTY_MEDIA_ID = "EMPTY_MEDIA_ID";
    final int EMPTY_VIEW_TYPE = -1;
    public abstract Category getSubscriptionCategory();
    final AlbumArtPainter albumArtPainter;

    private List<MediaItem> items = new ArrayList<>();
    private List<MediaItem> filteredSongs = new ArrayList<>();
    private boolean isInitialised = false;
    private final MediaItem EMPTY_LIST_ITEM = buildEmptyListMediaItem();

    public MyGenericRecycleViewAdapter(AlbumArtPainter albumArtPainter) {
        super();
        this.albumArtPainter = albumArtPainter;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public int getItemCount() {
        return getFilteredSongs() == null ? 0: getFilteredSongs().size();
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaItem> children, @NonNull Bundle options) {
        if (!isInitialised && children.isEmpty()) {
            addNoChildrenFoundItem();
        }

        if (!children.isEmpty()) {
            this.filteredSongs.addAll(children);
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
        filteredSongs.add(EMPTY_LIST_ITEM);
        getItems().add(EMPTY_LIST_ITEM);
        notifyDataSetChanged();
    }

    private MediaItem buildEmptyListMediaItem() {
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setMediaId(EMPTY_MEDIA_ID)
                .build();
        return new MediaItem(mediaDescriptionCompat, MediaItem.FLAG_PLAYABLE);
    }

    public List<MediaItem> getFilteredSongs() {
        return filteredSongs;
    }

    public List<MediaItem> getItems() {
        return items;
    }

    protected boolean isEmptyRecycleView() {
        return getItems().isEmpty() || getItems().get(FIRST).equals(EMPTY_LIST_ITEM);
    }

    @VisibleForTesting
    public void setFilteredSongs(List<MediaItem> filteredSongs) {
        this.filteredSongs = filteredSongs;
    }

    @VisibleForTesting
    public void setItems(List<MediaItem> items) {
        this.items = items;
    }

    @Override
    public String getSectionName(int position) {
        return getFilteredSongs().get(position).getDescription().getTitle().toString().substring(0, 1);
    }

    protected class MySongFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<MediaItem> filteredList = new ArrayList<>();

            if (StringUtils.isBlank(constraint.toString())) {
                return results(getItems());
            }

            for (MediaItem i : getItems()) {
                String title = hasTitle(i) ? getTitle(i).toUpperCase(Locale.getDefault()) : null;
                String uppercaseConstraint = constraint.toString().toUpperCase(Locale.getDefault());
                if (null != title && title.contains(uppercaseConstraint)) {
                    filteredList.add(i);
                }
            }
            return results(filteredList);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            setFilteredSongs((List<MediaItem>) results.values);
            notifyDataSetChanged();
        }

        private FilterResults results(List<MediaItem> filteredSongs) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredSongs;
            return filterResults;
        }
    }
}
