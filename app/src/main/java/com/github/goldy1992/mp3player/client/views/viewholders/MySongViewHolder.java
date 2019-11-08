package com.github.goldy1992.mp3player.client.views.viewholders;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.client.utils.TimerUtils;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;
import com.github.goldy1992.mp3player.commons.MetaDataKeys;

import org.apache.commons.io.FilenameUtils;

import static com.github.goldy1992.mp3player.commons.Constants.UNKNOWN;
import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getAlbumArtUri;
import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getExtra;
import static com.github.goldy1992.mp3player.commons.MediaItemUtils.hasExtras;

public class MySongViewHolder extends MediaItemViewHolder {

    private final TextView title;
    private final TextView artist;
    private final TextView duration;
    private final ImageView albumArt;

    public MySongViewHolder(@NonNull View itemView, AlbumArtPainter albumArtPainter) {
        super(itemView, albumArtPainter);
        this.artist = itemView.findViewById(R.id.artist);
        this.title = itemView.findViewById(R.id.title);
        this.duration = itemView.findViewById(R.id.duration);
        this.albumArt = itemView.findViewById(R.id.song_item_album_art);
    }

    @Override
    public void bindMediaItem(MediaBrowserCompat.MediaItem item) {
        // - get element from your dataset at this position
        // - replace the contents of the views with that element
        String title = extractTitle(item);
        String artist = extractArtist(item);
        String duration = extractDuration(item);
        this.artist.setText(artist);
        this.title.setText(title);
        this.duration.setText(duration);
        Uri uri = getAlbumArtUri(item);
        albumArtPainter.paintOnView(albumArt, uri);
    }

    private String extractTitle(MediaBrowserCompat.MediaItem song) {
        CharSequence charSequence = MediaItemUtils.getTitle(song);
        if (null == charSequence) {
            String fileName = hasExtras(song) ? (String) getExtra(MetaDataKeys.META_DATA_KEY_FILE_NAME, song) : null;
            if (fileName != null) {
                return FilenameUtils.removeExtension(fileName);
            }
        } else {
            return charSequence.toString();
        }
        return UNKNOWN;
    }

    private String extractDuration(@NonNull MediaBrowserCompat.MediaItem song) {
        Bundle extras =  song.getDescription().getExtras();
        if (null != extras) {
            long duration = extras.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
            return TimerUtils.formatTime(duration);
        }
        return null;
    }

    private String extractArtist(MediaBrowserCompat.MediaItem song) {
        String artist = null;
        try {
            artist = MediaItemUtils.getArtist(song);
            if (null == artist) {
                artist = UNKNOWN;
            }
        } catch (NullPointerException ex) {
            artist = UNKNOWN;
        }
        return artist;
    }

    @VisibleForTesting
    public TextView getTitle() { return title; }
    @VisibleForTesting
    public TextView getArtist() { return artist; }
    @VisibleForTesting
    public TextView getDuration() { return duration; }
    @VisibleForTesting
    public ImageView getAlbumArt() { return albumArt; }
}
