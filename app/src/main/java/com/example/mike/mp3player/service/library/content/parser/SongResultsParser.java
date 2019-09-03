package com.example.mike.mp3player.service.library.content.parser;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemBuilder;
import com.example.mike.mp3player.commons.MediaItemType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.Nullable;

import static android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE;
import static com.example.mike.mp3player.commons.ComparatorUtils.uppercaseStringCompare;
import static com.example.mike.mp3player.commons.MediaItemUtils.getMediaUri;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;

public class SongResultsParser extends ResultsParser {

    public static final String ALBUM_ART_URI_PREFIX = "content://media/external/audio/albumart";

    @Override
    public List<MediaItem> create(Cursor cursor, String libraryIdPrefix) {
        TreeSet<MediaItem> listToReturn = new TreeSet<>(this);
        while (cursor!= null && cursor.moveToNext()) {
            MediaItem mediaItem = buildMediaItem(cursor, libraryIdPrefix);
            listToReturn.add(mediaItem);
        }
        return new ArrayList<>(listToReturn);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.SONG;
    }

    private MediaItem buildMediaItem(Cursor c, String libraryIdPrefix) {
        final String mediaId = c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID));
        final String mediaFilePath = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
        final long duration = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
        final String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        final String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
        final long albumId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
        final File mediaFile =  new File(mediaFilePath);
        final Uri mediaUri = Uri.fromFile(mediaFile);
        final String fileName = mediaFile.getName();

        return new MediaItemBuilder(mediaId)
                .setMediaUri(mediaUri)
                .setTitle(title)
                .setLibraryId(buildLibraryId(libraryIdPrefix, mediaId))
                .setDuration(duration)
                .setFileName(fileName)
                .setArtist(artist)
                .setAlbumArtUri(albumId)
                .setFlags(FLAG_PLAYABLE)
                .build();
    }

    @Override
    public int compare(MediaItem m1, MediaItem m2) {
        int result = uppercaseStringCompare(getTitle(m1), getTitle(m2));
        return result == 0 ? getMediaUri(m1).compareTo(getMediaUri(m2)) : result;
    }

    private String buildLibraryId(@Nullable String mediaIdPrefix, String mediaIdSuffix) {
        if (mediaIdPrefix == null) {
            return mediaIdSuffix;
        } else {
            return mediaIdPrefix + "|" + mediaIdSuffix;
        }
    }
}
