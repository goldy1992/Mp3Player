package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;
import static com.example.mike.mp3player.commons.MediaItemUtils.hasTitle;

public abstract class MyGenericRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> implements
        Filterable, MediaBrowserResponseListener {

    public abstract Category getSubscriptionCategory();
    MediaBrowserAdapter mediaBrowserAdapter;
    protected List<MediaBrowserCompat.MediaItem> items;
    protected List<MediaBrowserCompat.MediaItem> filteredSongs;
    MySongFilter filter;
    final String LOG_TAG = "MY_VIEW_ADAPTER";

    public MyGenericRecycleViewAdapter(MediaBrowserAdapter mediaBrowserAdapter, LibraryObject parent) {
        super();
        this.items = new ArrayList<>();
        this.mediaBrowserAdapter = mediaBrowserAdapter;
        this.filteredSongs = new ArrayList<>();
        filter = new MySongFilter();
        mediaBrowserAdapter.registerListener(parent.getId(), this);
        mediaBrowserAdapter.subscribe(new LibraryRequest(parent));
    }

    public void setData(List<MediaBrowserCompat.MediaItem> items) {
        if (this.getItems() == null) {
            this.items = items;
        } else {
            this.getItems().addAll(items);
        }
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
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options, Context context) {
        this.filteredSongs.addAll(children);
        this.items.addAll(children);
        notifyDataSetChanged();
    }

    public List<MediaBrowserCompat.MediaItem> getFilteredSongs() {
        return filteredSongs;
    }

    public List<MediaBrowserCompat.MediaItem> getItems() {
        return items;
    }

    protected class MySongFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<MediaBrowserCompat.MediaItem> filteredList = new ArrayList<>();

            if (StringUtils.isBlank(constraint.toString())) {
                return results(getItems());
            }

            for (MediaBrowserCompat.MediaItem i : getItems()) {
                String title = hasTitle(i) ? getTitle(i).toUpperCase(Locale.getDefault()) : null;
                String uppercaseConstraint = constraint.toString().toUpperCase(Locale.getDefault());
                if (title.contains(uppercaseConstraint)) {
                    filteredList.add(i);
                }
            }
            return results(filteredList);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredSongs = (List<MediaBrowserCompat.MediaItem>) results.values;
            notifyDataSetChanged();
        }

        private FilterResults results(List<MediaBrowserCompat.MediaItem> filteredSongs) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredSongs;
            return filterResults;
        }
    }


}
