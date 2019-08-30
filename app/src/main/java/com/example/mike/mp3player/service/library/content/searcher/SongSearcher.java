package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.builder.MediaItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.example.mike.mp3player.service.library.content.Projections.FOLDER_PROJECTION;
import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongSearcher extends ContentResolverSearcher {

    public SongSearcher(ContentResolver contentResolver, MediaItemBuilder mediaItemBuilder, String idPrefix) {
        super(contentResolver, mediaItemBuilder, idPrefix);
    }

    @Override
    String[] getProjection() {
        return SONG_PROJECTION;
    }

    @Override
    public MediaItemType getSearchCategory() {
        return MediaItemType.SONGS;
    }

    @Override
    public Cursor performSearchQuery(String query) {
        String WHERE_CLAUSE = MediaStore.Audio.Media.TITLE + " LIKE ? COLLATE NOCASE";
        String[] WHERE_ARGS = {"%" + query + "%"};
        return  contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                getProjection(),
                WHERE_CLAUSE, WHERE_ARGS, null);
    }
}
