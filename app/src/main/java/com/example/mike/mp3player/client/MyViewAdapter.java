package com.example.mike.mp3player.client;

import android.support.v4.media.MediaBrowserCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.view.MyViewHolder;
import com.example.mike.mp3player.commons.MetaDataKeys;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyViewAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {
    private List<MediaBrowserCompat.MediaItem> songs;
    private List<MediaBrowserCompat.MediaItem> filteredSongs;
    private MySongFilter filter;
    private final String LOG_TAG = "MY_VIEW_ADAPTER";


    public MyViewAdapter(List<MediaBrowserCompat.MediaItem> songs) {
        super();
        this.songs = songs;
        this.filteredSongs = songs;
        filter = new MySongFilter();
    }
    public void setData(List<MediaBrowserCompat.MediaItem> items) {
        if (getSongs() == null) {
            this.songs = items;
        } else {
            getSongs().addAll(items);
        }
    }
        @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        GridLayout t = (GridLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item_menu, parent, false);

        MyViewHolder vh = new MyViewHolder(t);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Log.i(LOG_TAG, "position: " + position);
        MediaBrowserCompat.MediaItem song = getFilteredSongs().get(holder.getAdapterPosition());
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        song.getMediaId();
        String title = extractTitle(song);
        String artist = extractArtist(song);
        String duration = extractDuration(song);

        TextView artistText = holder.getView().findViewById(R.id.artist);
        artistText.setText(artist);

        TextView titleText = holder.getView().findViewById(R.id.title);
        titleText.setText(title);

        TextView durationText = holder.getView().findViewById(R.id.duration);
        durationText.setText(duration);
    }

    @Override
    public int getItemCount() {
        return getFilteredSongs() == null ? 0: getFilteredSongs().size();
    }

    private String extractTitle(MediaBrowserCompat.MediaItem song) {
        CharSequence charSequence = song.getDescription().getTitle();
        if (null == charSequence) {
            String fileName = (String) song.getDescription().getExtras().get(MetaDataKeys.META_DATA_KEY_FILE_NAME);
            if (fileName != null) {
                return FilenameUtils.removeExtension(fileName);
            }
        } else {
            return charSequence.toString();
        }
        return "";
    }

    private String extractArtist(MediaBrowserCompat.MediaItem song) {
        String artist = song.getDescription().getExtras().getString(MetaDataKeys.STRING_METADATA_KEY_ARTIST);
        if (null == artist) {
            artist = "Unknown";
        }
        return artist;
    }

    private String extractDuration(MediaBrowserCompat.MediaItem song) {
        String durationString = song.getDescription().getExtras().getString(MetaDataKeys.STRING_METADATA_KEY_DURATION);
        long duration = Long.parseLong(durationString);
        return TimerUtils.formatTime(duration);
    }

    public List<MediaBrowserCompat.MediaItem> getSongs() {
        return songs;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public List<MediaBrowserCompat.MediaItem> getFilteredSongs() {
        return filteredSongs;
    }

    private class MySongFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<MediaBrowserCompat.MediaItem> filteredList = new ArrayList<>();

            if (StringUtils.isBlank(constraint.toString())) {
                return results(songs);
            }

            for (MediaBrowserCompat.MediaItem i : songs) {
                String title = i.getDescription().getTitle().toString().toUpperCase(Locale.getDefault());
                String uppercaseConstraint = constraint.toString().toUpperCase(Locale.getDefault());
                if (title.contains(uppercaseConstraint)) {
                    filteredList.add(i);
                }
            }
            return results(filteredList);
        }


        @Override
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
