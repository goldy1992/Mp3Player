package com.example.mike.mp3player.service.library;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ALBUM_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_DURATION;

public class ContentResolverMediaRetriever extends MediaRetriever {
    private ContentResolver m_contentResolver;
    public ContentResolverMediaRetriever(Context context) {
        super(context);
        this.m_contentResolver = context.getContentResolver();
    }

    final String[] PROJECTION = {Media.DATA, Media.DURATION, Media.ARTIST, Media.TITLE};
    @Override
    List<MediaBrowserCompat.MediaItem> retrieveMedia() {
        List<MediaBrowserCompat.MediaItem> listToReturn = new ArrayList<>();
        Cursor cursor = m_contentResolver.query(Media.EXTERNAL_CONTENT_URI ,PROJECTION,
                null, null, null);
        cursor.getCount();
        while (cursor.moveToNext()) {
            listToReturn.add(createPlayableMediaItemFromCursor(cursor));
        }
        return listToReturn;
    }

    private MediaBrowserCompat.MediaItem createPlayableMediaItemFromCursor(Cursor c){
        String path = c.getString(c.getColumnIndex(Media.DATA));
        long duration = c.getLong(c.getColumnIndex(Media.DURATION));
        String artist = c.getString(c.getColumnIndex(Media.ARTIST));
        String title = c.getString(c.getColumnIndex(Media.TITLE));
        File file =  new File(path);
        File directory = file.getParentFile();


        Uri uri = Uri.fromFile(file);
        String mediaId = String.valueOf(uri.getPath().hashCode());
        String parentPath = file.getParentFile().getAbsolutePath();
        String fileName = file.getName();

        Bundle extras = new Bundle();
        extras.putString(STRING_METADATA_KEY_DURATION, String.valueOf(duration));
        extras.putString(STRING_METADATA_KEY_ARTIST, artist);
        extras.putString(META_DATA_KEY_PARENT_PATH, parentPath);
        extras.putString(META_DATA_KEY_FILE_NAME, fileName);
        extras.putString(META_DATA_PARENT_DIRECTORY_NAME, directory.getName());
        extras.putString(META_DATA_PARENT_DIRECTORY_PATH, directory.getAbsolutePath());

        // TODO: add code to fetch album art also
        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setMediaUri(uri)
                .setTitle(title)
                .setExtras(extras);

        MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
        return mediaItem;
    }
}
