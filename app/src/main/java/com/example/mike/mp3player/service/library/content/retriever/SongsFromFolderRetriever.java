package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.search.SearchDatabase;

import java.util.List;

import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongsFromFolderRetriever extends ContentResolverRetriever {
    public SongsFromFolderRetriever(ContentResolver contentResolver, ResultsParser resultsParser, SearchDatabase searchDatabase) {
        super(contentResolver, resultsParser, searchDatabase);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.SONG;
    }

    @Override
    Cursor performGetChildrenQuery(String id) {
        String WHERE_CLAUSE = MediaStore.Audio.Media.DATA + " LIKE ? ";
        String[] WHERE_ARGS = {id + "%"};
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,getProjection(),
                WHERE_CLAUSE , WHERE_ARGS, null);
    }

    @Override
    void updateSearchDatabase(List<MediaBrowserCompat.MediaItem> results) {

    }

    @Override
    String[] getProjection() {
        return SONG_PROJECTION.toArray(new String[0]);
    }

}
