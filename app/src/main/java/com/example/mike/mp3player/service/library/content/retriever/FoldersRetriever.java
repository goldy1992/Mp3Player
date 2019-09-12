package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.search.SearchDatabase;

import java.util.List;

import static com.example.mike.mp3player.service.library.content.Projections.FOLDER_PROJECTION;

public class FoldersRetriever extends ContentResolverRetriever {

    public FoldersRetriever(ContentResolver contentResolver, ResultsParser resultsParser,
                            SearchDatabase searchDatabase) {
        super(contentResolver, resultsParser, searchDatabase);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
    }


    @Override
    Cursor performGetChildrenQuery(String id) {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,getProjection(),
                null, null, null);
    }

    @Override
    void updateSearchDatabase(List<MediaBrowserCompat.MediaItem> results) {

    }

    @Override
    public String[] getProjection() {
        return FOLDER_PROJECTION.toArray(new String[0]);
    }

}
