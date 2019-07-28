package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import java.io.File;
import java.util.List;

import javax.inject.Singleton;

import static android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

@Singleton
public abstract class MediaRetriever {
    Context context;

    public MediaRetriever(Context context) {
        this.context = context;
    }
    public abstract List<MediaItem> retrieveMedia();

    public MediaBrowserCompat.MediaItem createPlayableMediaItemFromFile(File file, File directory) {
        Uri uri = Uri.fromFile(file);
        String mediaId = String.valueOf(uri.getPath().hashCode());
        String parentPath = file.getParentFile().getAbsolutePath();
        String fileName = file.getName();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);

        Bundle extras = new Bundle();
        extras.putString(MediaMetadataCompat.METADATA_KEY_DURATION, mmr.extractMetadata(METADATA_KEY_DURATION));
        extras.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mmr.extractMetadata(METADATA_KEY_ARTIST));
        extras.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, mmr.extractMetadata(METADATA_KEY_ALBUMARTIST));
        extras.putString(META_DATA_KEY_PARENT_PATH, parentPath);
        extras.putString(META_DATA_KEY_FILE_NAME, fileName);
        extras.putString(META_DATA_PARENT_DIRECTORY_NAME, directory.getName());
        extras.putString(META_DATA_PARENT_DIRECTORY_PATH, directory.getAbsolutePath());

        // TODO: add code to fetch album art also
        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setMediaUri(uri)
                .setTitle(MediaLibraryUtils.getSongTitle(mmr, fileName))
                .setExtras(extras);

        MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
        return mediaItem;
    }

    public Context getContext() {
        return context;
    }
}
