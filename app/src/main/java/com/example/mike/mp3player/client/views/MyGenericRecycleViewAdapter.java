package com.example.mike.mp3player.client.views;

import android.support.v4.media.MediaBrowserCompat;
import android.widget.Filter;
import android.widget.Filterable;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public abstract class MyGenericRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    List<MediaBrowserCompat.MediaItem> items;
    List<MediaBrowserCompat.MediaItem> filteredSongs;
    MySongFilter filter;
    final String LOG_TAG = "MY_VIEW_ADAPTER";

    public MyGenericRecycleViewAdapter(List<MediaBrowserCompat.MediaItem> songs) {
        super();
        this.items = songs;
        this.filteredSongs = songs;
        filter = new MySongFilter();
    }


    public void setData(List<MediaBrowserCompat.MediaItem> items) {
        if (this.items == null) {
            this.items = items;
        } else {
            this.items.addAll(items);
        }
    }

    protected class MySongFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<MediaBrowserCompat.MediaItem> filteredList = new ArrayList<>();

            if (StringUtils.isBlank(constraint.toString())) {
                return results(items);
            }

            for (MediaBrowserCompat.MediaItem i : items) {
                String title = i.getDescription().getTitle().toString().toUpperCase(Locale.getDefault());
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
