package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;

import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;
import com.github.goldy1992.mp3player.service.library.search.SongDao;

import static com.github.goldy1992.mp3player.TestConstants.TEST_DATA_DIR;

public class SongsRetrieverTestImpl extends SongsRetriever {

    public SongsRetrieverTestImpl(ContentResolver contentResolver, SongResultsParser resultsParser, SongDao songDao, Handler handler) {
        super(contentResolver, resultsParser, handler);
    }

    private final String WHERE_CLAUSE = MediaStore.Audio.Media.DATA + " LIKE ?";


    private final String[] WHERE_ARGS = {"%" + TEST_DATA_DIR + "%"};
    @Override
    Cursor performGetChildrenQuery(String id) {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getProjection(),
                WHERE_CLAUSE, WHERE_ARGS, null);
    }
}
