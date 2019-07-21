package com.example.mike.mp3player.client;

import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.views.MyGenericRecycleViewAdapter;
import com.example.mike.mp3player.client.views.MyViewHolder;
import com.example.mike.mp3player.commons.MetaDataKeys;
import com.example.mike.mp3player.commons.library.Category;

import org.apache.commons.io.FilenameUtils;

import static com.example.mike.mp3player.commons.MediaItemUtils.getExtra;
import static com.example.mike.mp3player.commons.MediaItemUtils.hasExtras;

public class MySongViewAdapter extends MyGenericRecycleViewAdapter {
    private final String LOG_TAG = "MY_VIEW_ADAPTER";

    @Override
    public Category getSubscriptionCategory() {
        return Category.SONGS;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder vh = super.onCreateViewHolder(parent, viewType);
        if (vh == null) {
            // create a new views
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            GridLayout t = (GridLayout) layoutInflater
                    .inflate(R.layout.song_item_menu, parent, false);

            vh = new MyViewHolder(t);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder( @NonNull MyViewHolder holder, int position) {
        if (!isEmptyRecycleView()) {
            // TODO: look into the use of holder.getAdapterPosition rather than the position parameter.
            //Log.i(LOG_TAG, "position: " + position);
            MediaBrowserCompat.MediaItem song = getFilteredSongs().get(holder.getAdapterPosition());
            // - get element from your dataset at this position
            // - replace the contents of the views with that element
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
    }


    private String extractTitle(MediaBrowserCompat.MediaItem song) {
        CharSequence charSequence = song.getDescription().getTitle();
        if (null == charSequence) {
            String fileName = hasExtras(song) ? (String) getExtra(MetaDataKeys.META_DATA_KEY_FILE_NAME, song) : null;
            if (fileName != null) {
                return FilenameUtils.removeExtension(fileName);
            }
        } else {
            return charSequence.toString();
        }
        return "";
    }

    private String extractArtist(MediaBrowserCompat.MediaItem song) {
        String artist = null;
        try {
            artist = song.getDescription().getExtras().getString(MetaDataKeys.STRING_METADATA_KEY_ARTIST);
            if (null == artist) {
                artist = "Unknown";
            }
        } catch (NullPointerException ex) {
            artist = "Unknown";
        }
        return artist;
    }

    private String extractDuration(MediaBrowserCompat.MediaItem song) {
        String durationString = song.getDescription().getExtras().getString(MetaDataKeys.STRING_METADATA_KEY_DURATION);
        long duration = Long.parseLong(durationString);
        return TimerUtils.formatTime(duration);
    }
}
