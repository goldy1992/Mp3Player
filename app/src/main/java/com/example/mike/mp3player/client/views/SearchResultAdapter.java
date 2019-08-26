package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.MetaDataKeys;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MediaItemUtils.getExtra;
import static com.example.mike.mp3player.commons.MediaItemUtils.hasExtras;

public class SearchResultAdapter extends RecyclerView.Adapter {

    public List<MediaBrowserCompat.MediaItem> items = new ArrayList<>();
    private AlbumArtPainter albumArtPainter;
    public SearchResultAdapter(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.albumArtPainter = new AlbumArtPainter(context, handler);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MediaItemType.SONG.getValue()) {
            return new MySongViewHolder(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MySongViewHolder mySongViewHolder = (MySongViewHolder) holder;
            // TODO: look into the use of holder.getAdapterPosition rather than the position parameter.
            //Log.i(LOG_TAG, "position: " + position);
            MediaBrowserCompat.MediaItem song = items.get(holder.getAdapterPosition());
            // - get element from your dataset at this position
            // - replace the contents of the views with that element
            String title = extractTitle(song);
            String artist = extractArtist(song);
            String duration = extractDuration(song);

            mySongViewHolder.titleArtistView.setText(artist);
            mySongViewHolder.titleTextView.setText(title);
            mySongViewHolder.titleDurationView.setText(duration);
            ImageView albumArt = mySongViewHolder.albumArt;
            Uri uri = getAlbumArtUri(song);
            albumArtPainter.paintOnView(albumArt, uri);
    }

    @Override
    public int getItemViewType(int position) {
        MediaBrowserCompat.MediaItem mediaItem = items.get(position);
        MediaItemType mediaItemType = (MediaItemType) MediaItemUtils.getExtra(MEDIA_ITEM_TYPE, mediaItem);
        if (null != mediaItemType) {
            return mediaItemType.getValue();
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private Uri getAlbumArtUri(MediaBrowserCompat.MediaItem song) {
        Uri albumUri = (Uri) song.getDescription().getExtras().get(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI);
        return albumUri;
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
            artist = song.getDescription().getExtras().getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
            if (null == artist) {
                artist = UNKNOWN;
            }
        } catch (NullPointerException ex) {
            artist = UNKNOWN;
        }
        return artist;
    }

    private String extractDuration(MediaBrowserCompat.MediaItem song) {
        long duration = song.getDescription().getExtras().getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        return TimerUtils.formatTime(duration);
    }
}
