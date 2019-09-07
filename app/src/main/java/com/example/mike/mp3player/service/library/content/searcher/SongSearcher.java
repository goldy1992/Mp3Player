package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;

import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongSearcher extends ContentResolverSearcher {

    public SongSearcher(ContentResolver contentResolver, ResultsParser resultsParser,  String idPrefix) {
        super(contentResolver, resultsParser, null, idPrefix);
    }

    @Override
    String[] getProjection() {
        return SONG_PROJECTION.toArray(new String[0]);
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
