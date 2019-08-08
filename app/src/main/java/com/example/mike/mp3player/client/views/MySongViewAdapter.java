package com.example.mike.mp3player.client.views;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaMetadataCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.commons.MetaDataKeys;
import com.example.mike.mp3player.service.library.Category;

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
    public Category getSubscriptionCategory() {
        return Category.SONGS;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder vh = super.onCreateViewHolder(parent, viewType);
        if (vh == null) {
            // create a new views
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ViewGroup v = (ViewGroup) layoutInflater
                    .inflate(R.layout.song_item_menu, parent, false);

            vh = new MyViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder( @NonNull MyViewHolder holder, int position) {
        if (!isEmptyRecycleView()) {
            // TODO: look into the use of holder.getAdapterPosition rather than the position parameter.
            //Log.i(LOG_TAG, "position: " + position);
            MediaItem song = getItems().get(holder.getAdapterPosition());
            // - get element from your dataset at this position
            // - replace the contents of the views with that element
            String title = extractTitle(song);
            String artist = extractArtist(song);
            String duration = extractDuration(song);

            TextView artistText = holder.getView().findViewById(R.id.artist);
            artistText.setText(artist);

            TextView titleText = holder.getView().findViewById(R.id.title);
            titleText.setText(title);

            TextView durationText = holder.getView().findViewById(R.id.duration);
            durationText.setText(duration);

            ImageView albumArt = holder.getView().findViewById(R.id.song_item_album_art);
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
