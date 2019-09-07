package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;

import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongsRetriever extends ContentResolverRetriever {

    public SongsRetriever(ContentResolver contentResolver,
                          ResultsParser resultsParser) {
        super(contentResolver, resultsParser);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.SONG;
    }

    @Override
    Cursor performGetChildrenQuery(String id) {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getProjection(),
                null, null, null);
    }

    @Override
    public String[] getProjection() {
        return SONG_PROJECTION.toArray(new String[0]);
    }
}
