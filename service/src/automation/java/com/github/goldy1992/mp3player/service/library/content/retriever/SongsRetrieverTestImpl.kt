package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import android.os.Handler
import android.provider.MediaStore
import com.github.goldy1992.mp3player.service.TestConstants.TEST_DATA_DIR
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.search.SongDao

class SongsRetrieverTestImpl(contentResolver: ContentResolver, resultsParser: SongResultsParser, songDao: SongDao)

    : SongsRetriever(contentResolver, resultsParser) {
    private val WHERE_CLAUSE = MediaStore.Audio.Media.DATA + " LIKE ?"
    private val WHERE_ARGS = arrayOf("%" + TEST_DATA_DIR.toString() + "%")
    override fun performGetChildrenQuery(id: String?): Cursor? {
        return null
        //contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getProjection(),
//       WHERE_CLAUSE, WHERE_ARGS, null);
    }
}