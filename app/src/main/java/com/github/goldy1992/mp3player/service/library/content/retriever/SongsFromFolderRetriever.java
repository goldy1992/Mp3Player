package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.content.filter.SongsFromFolderResultsFilter;
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;

import javax.inject.Inject;
import javax.inject.Named;

import static com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongsFromFolderRetriever extends ContentResolverRetriever {

    private static final String WHERE = MediaStore.Audio.Media.DATA + " LIKE ? ";

    @Inject
    public SongsFromFolderRetriever(ContentResolver contentResolver,
                                    SongResultsParser resultsParser,
                                    @Named("worker") Handler handler,
                                    SongsFromFolderResultsFilter songsFromFolderResultsFilter) {
        super(contentResolver, resultsParser, handler, songsFromFolderResultsFilter);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.SONG;
    }

    @Override
    Cursor performGetChildrenQuery(String id) {
        String[] whereArgs = {id + "%"};
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,getProjection(),
                WHERE, whereArgs, null);
    }

    @Override
    String[] getProjection() {
        return SONG_PROJECTION.toArray(new String[0]);
    }

}
