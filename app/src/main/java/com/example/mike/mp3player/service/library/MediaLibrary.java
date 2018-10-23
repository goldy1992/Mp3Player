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

    private boolean playlistRecursInSubDirectory = false;
    private Map<File, List<MediaBrowserCompat.MediaItem>> library;
    private MusicFileFilter musicFileFilter = new MusicFileFilter();
    private IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();
    private Context context;

    public MediaLibrary(Context context)
    {
        this.context = context;
    }
    public void init() {
        library = new HashMap<>();
        buildMediaLibrary();
    }

    public void buildMediaLibrary(){
        File externalStorageDirectory = MediaLibraryUtils.getExternalStorageDirectory();
        buildDirectoryPlaylist(externalStorageDirectory);
    }

    private void buildDirectoryPlaylist(File directory)
    {
        List<File> directories = new ArrayList<>();
        List<MediaBrowserCompat.MediaItem> files = new ArrayList<>();

        if (directory.list() != null) {
            for (String currentFileString : directory.list()) {
                if (isDirectoryFilter.accept(directory, currentFileString)) {
                    directories.add(new File(directory, currentFileString));
                } else if (musicFileFilter.accept(directory, currentFileString)) {
                    files.add(createMediaItemFromFile(new File(directory, currentFileString)));
                }
            } // for
        }

        if (!files.isEmpty()) {
            getLibrary().put(directory, files);
        }

        if (!directories.isEmpty())
        {
            for (File directoryToBrowse : directories) {
                buildDirectoryPlaylist(directoryToBrowse);
            }
        }
    }

    public Map<File, List<MediaBrowserCompat.MediaItem>> getLibrary() {
        return library;
    }

    private MediaBrowserCompat.MediaItem createMediaItemFromFile(File file) {
        Uri uri = Uri.fromFile(file);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);

        Bundle extras = new Bundle();
        extras.putString(String.valueOf(METADATA_KEY_DURATION), mmr.extractMetadata(METADATA_KEY_DURATION));
        extras.putString(String.valueOf(METADATA_KEY_ARTIST), mmr.extractMetadata(METADATA_KEY_ARTIST));
        extras.putString(String.valueOf(METADATA_KEY_ALBUMARTIST), mmr.extractMetadata(METADATA_KEY_ALBUMARTIST));
        extras.putString(String.valueOf(METADATA_KEY_DURATION), mmr.extractMetadata(METADATA_KEY_DURATION));
        // TODO: add more items, including link to parent directory in order to organise tracks by folder.
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
