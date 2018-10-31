package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.service.library.utils.IsDirectoryFilter;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;
import com.example.mike.mp3player.service.library.utils.MusicFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;
import static android.media.MediaMetadataRetriever.METADATA_KEY_TITLE;

public class MediaLibrary {

    public static final String META_DATA_KEY_PARENT_PATH = "META_DATA_KEY_PARENT_PATH";

    private boolean playlistRecursInSubDirectory = false;
    private List<MediaBrowserCompat.MediaItem> library;
    private MusicFileFilter musicFileFilter = new MusicFileFilter();
    private IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();
    private Context context;

    public MediaLibrary(Context context)
    {
        this.context = context;
    }
    public void init() {
        library = new ArrayList<>();
        buildMediaLibrary();
    }

    public void buildMediaLibrary(){
        File externalStorageDirectory = MediaLibraryUtils.getExternalStorageDirectory();
        buildDirectoryPlaylist(externalStorageDirectory);
    }

    private void buildDirectoryPlaylist(File directory)
    {
        List<File> directories = new ArrayList<>();
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();

        if (directory.list() != null) {
            for (String currentFileString : directory.list()) {
                if (isDirectoryFilter.accept(directory, currentFileString)) {
                    directories.add(new File(directory, currentFileString));
                } else if (musicFileFilter.accept(directory, currentFileString)) {
                    mediaItems.add(createMediaItemFromFile(new File(directory, currentFileString)));
                }
            } // for
        }

        if (!directories.isEmpty()) {
            for (File directoryToBrowse : directories) {
                buildDirectoryPlaylist(directoryToBrowse);
            }
        }

        library.addAll(mediaItems);
    }

    public List<MediaBrowserCompat.MediaItem> getLibrary() {
        return library;
    }

    private MediaBrowserCompat.MediaItem createMediaItemFromFile(File file) {
        Uri uri = Uri.fromFile(file);
        String parentPath = file.getParentFile().getAbsolutePath();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);

        Bundle extras = new Bundle();
        extras.putString(String.valueOf(METADATA_KEY_DURATION), mmr.extractMetadata(METADATA_KEY_DURATION));
        extras.putString(String.valueOf(METADATA_KEY_ARTIST), mmr.extractMetadata(METADATA_KEY_ARTIST));
        extras.putString(String.valueOf(METADATA_KEY_ALBUMARTIST), mmr.extractMetadata(METADATA_KEY_ALBUMARTIST));
        extras.putString(String.valueOf(METADATA_KEY_DURATION), mmr.extractMetadata(METADATA_KEY_DURATION));
        extras.putString(META_DATA_KEY_PARENT_PATH, parentPath);

        // TODO: add code to fetch album art also
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setMediaId(String.valueOf(uri.getPath().hashCode()))
                .setTitle(mmr.extractMetadata(METADATA_KEY_DURATION))
                .setExtras(extras)
                .build();
        MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescriptionCompat, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
        return mediaItem;
    }
}
