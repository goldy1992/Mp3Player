package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.Constants.FIRST;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;
import static com.example.mike.mp3player.commons.MediaItemUtils.hasTitle;

public abstract class MyGenericRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> implements
        Filterable, MediaBrowserResponseListener {
    final String LOG_TAG = "MY_VIEW_ADAPTER";
    private static final String EMPTY_MEDIA_ID = "EMPTY_MEDIA_ID";
    final int EMPTY_VIEW_TYPE = -1;
    public abstract Category getSubscriptionCategory();
    MediaBrowserAdapter mediaBrowserAdapter;
    protected List<MediaItem> items = new ArrayList<>();;
    protected List<MediaItem> filteredSongs = new ArrayList<>();;
    MySongFilter filter;
    private boolean isInitialised = false;
    private final MediaItem EMPTY_LIST_ITEM = buildEmptyListMediaItem();

    public MyGenericRecycleViewAdapter(MediaBrowserAdapter mediaBrowserAdapter, LibraryObject parent) {
        super();
        this.mediaBrowserAdapter = mediaBrowserAdapter;
        filter = new MySongFilter();
        mediaBrowserAdapter.registerListener(parent.getId(), this);
        mediaBrowserAdapter.subscribe(new LibraryRequest(parent));
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public int getItemCount() {
        return getFilteredSongs() == null ? 0: getFilteredSongs().size();
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaItem> children, @NonNull Bundle options, Context context) {
        if (!isInitialised && children.isEmpty()) {
            addNoChildrenFoundItem();
        }

        if (!children.isEmpty()) {
            this.filteredSongs.addAll(children);
            this.items.addAll(children);
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
        items.add(EMPTY_LIST_ITEM);
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
        return items.isEmpty() || items.get(FIRST).equals(EMPTY_LIST_ITEM);
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
            filteredSongs = (List<MediaItem>) results.values;
            notifyDataSetChanged();
        }

        private FilterResults results(List<MediaItem> filteredSongs) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredSongs;
            return filterResults;
        }
    }
}
