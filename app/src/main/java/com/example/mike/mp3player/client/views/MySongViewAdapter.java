package com.example.mike.mp3player.client.views;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaMetadataCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.commons.MetaDataKeys;

import org.apache.commons.io.FilenameUtils;

import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MediaItemUtils.getExtra;
import static com.example.mike.mp3player.commons.MediaItemUtils.hasExtras;


public class MySongViewAdapter extends MyGenericRecycleViewAdapter {
    private final String LOG_TAG = "MY_VIEW_ADAPTER";

    public MySongViewAdapter(AlbumArtPainter albumArtPainter) {
        super(albumArtPainter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = super.onCreateViewHolder(parent, viewType);
        if (vh == null) {
            // create a new views
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View v = layoutInflater
                    .inflate(R.layout.song_item_menu, parent, false);
            vh = new MySongViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final boolean isSongHolder = holder instanceof MySongViewHolder;
        if (isSongHolder && !isEmptyRecycleView()) {
            MySongViewHolder songViewHolder = (MySongViewHolder) holder;
            // TODO: look into the use of holder.getAdapterPosition rather than the position parameter.
            //Log.i(LOG_TAG, "position: " + position);
            MediaItem song = getItems().get(holder.getAdapterPosition());
            // - get element from your dataset at this position
            // - replace the contents of the views with that element
            String title = extractTitle(song);
            String artist = extractArtist(song);
            String duration = extractDuration(song);

            songViewHolder.titleArtistView.setText(artist);
            songViewHolder.titleTextView.setText(title);
            songViewHolder.titleDurationView.setText(duration);
            ImageView albumArt = songViewHolder.albumArt;
            Uri uri = getAlbumArtUri(song);
            albumArtPainter.paintOnView(albumArt, uri);
        }
    }

    private Uri getAlbumArtUri(MediaItem song) {
        Uri albumUri = (Uri) song.getDescription().getExtras().get(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI);
        return albumUri;
    }

    private String extractTitle(MediaItem song) {
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

    private String extractArtist(MediaItem song) {
        String artist = null;
        try {
            artist = song.getDescription().getExtras().getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
            if (null == artist) {
                artist = UNKNOWN;
            }
        } catch (NullPointerException ex) {
            artist = UNKNOWN;
        }
        return artist;
    }

    private String extractDuration(MediaItem song) {
        long duration = song.getDescription().getExtras().getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        return TimerUtils.formatTime(duration);
    }
}
