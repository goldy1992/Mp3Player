package com.example.mike.mp3player.service.library.db;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import androidx.room.TypeConverter;

import com.example.mike.mp3player.commons.library.Category;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.Constants.ARTWORK_URI_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;


public class MediaItemConverter {

    @TypeConverter
    public static MediaItem fromSongToMediaItem(Song song) {

        Folder folder = song.folder;
        Bundle extras = new Bundle();

        Uri albumArtUri = ContentUris.withAppendedId(ARTWORK_URI_PATH, song.albumId);
        extras.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.duration);
        extras.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.artist);
        extras.putString(META_DATA_KEY_PARENT_PATH, folder.path);
        extras.putString(META_DATA_KEY_FILE_NAME, folder.name);
        extras.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, albumArtUri.toString());

        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setMediaId(song.uri)
                .setTitle(song.title)
                .setMediaUri(Uri.parse(song.uri))
                .setExtras(extras)
        .build();

        return new MediaItem(mediaDescriptionCompat, MediaItem.FLAG_PLAYABLE);
    }


}
