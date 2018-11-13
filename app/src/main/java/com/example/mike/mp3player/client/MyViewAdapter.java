package com.example.mike.mp3player.client;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.view.MyViewHolder;
import com.example.mike.mp3player.commons.MetaDataKeys;

import org.apache.commons.io.FilenameUtils;

import java.util.List;

public class MyViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<MediaBrowserCompat.MediaItem> songs;
    private MyViewHolder myViewHolder;


    public MyViewAdapter(List<MediaBrowserCompat.MediaItem> songs) {
        super();
        this.songs = songs;
    }
    public void setData(List<MediaBrowserCompat.MediaItem> items) {
        if (songs == null) {
            this.songs = items;
        } else {
            songs.addAll(items);
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
        MediaBrowserCompat.MediaItem song = songs.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
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
        return songs == null ? 0: songs.size();
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

}
