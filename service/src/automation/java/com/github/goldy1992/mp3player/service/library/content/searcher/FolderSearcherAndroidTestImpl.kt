package com.github.goldy1992.mp3player.service.library.content.searcher

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.github.goldy1992.mp3player.service.TestConstants
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.filter.FolderSearchResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.search.Folder
import com.github.goldy1992.mp3player.service.library.search.FolderDao
import kotlinx.coroutines.CoroutineScope
import org.apache.commons.lang3.StringUtils
import java.util.ArrayList

class FolderSearcherAndroidTestImpl

    constructor(contentResolver: ContentResolver,
                resultsParser: FolderResultsParser,
                folderSearchResultsFilter: FolderSearchResultsFilter,
                mediaItemTypeIds: MediaItemTypeIds,
                folderDao: FolderDao,
                scope : CoroutineScope)
            : FolderSearcher(contentResolver,
            resultsParser,
            folderSearchResultsFilter,
            mediaItemTypeIds,
            folderDao,
            scope) {

    override suspend fun performSearchQuery(query: String?): Cursor? {
        val results: List<Folder>? = searchDatabase.query(query)
        if (results != null && results.isNotEmpty()) {
            val parameters: MutableList<String> = ArrayList()
            val likeList: MutableList<String?> = ArrayList()
            val whereArgs: MutableList<String> = ArrayList()
            for (folder in results) {
                parameters.add(folder.id)
                likeList.add(LIKE_STATEMENT)
            }
            val WHERE = StringUtils.join(likeList, " OR ") + " AND " + MediaStore.Audio.Media.DATA + " LIKE ? COLLATE NOCASE"
            for (i in results.indices) {
                whereArgs.add(likeParam(parameters[i]))
            }
            whereArgs.add("%${TestConstants.TEST_DATA_DIR}%")
            val WHERE_ARGS = whereArgs.toTypedArray()
            return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                    WHERE, WHERE_ARGS, null)
        }
        return null
    }
}