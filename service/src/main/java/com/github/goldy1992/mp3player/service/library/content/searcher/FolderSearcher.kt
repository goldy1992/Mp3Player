package com.github.goldy1992.mp3player.service.library.content.searcher

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.Projections
import com.github.goldy1992.mp3player.service.library.content.filter.FolderSearchResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.search.Folder
import com.github.goldy1992.mp3player.service.library.search.FolderDao
import org.apache.commons.lang3.StringUtils
import java.util.*
import javax.inject.Inject

class FolderSearcher @Inject constructor(contentResolver: ContentResolver,
                                         resultsParser: FolderResultsParser,
                                         folderSearchResultsFilter: FolderSearchResultsFilter?,
                                         private val mediaItemTypeIds: MediaItemTypeIds,
                                         folderDao: FolderDao) : ContentResolverSearcher<Folder>(contentResolver, resultsParser, folderSearchResultsFilter, folderDao) {
    public override fun performSearchQuery(query: String?): Cursor? {
        val results: List<Folder>? = searchDatabase.query(query)
        if (results != null && !results.isEmpty()) {
            val ids: MutableList<String> = ArrayList()
            val likeList: MutableList<String?> = ArrayList()
            val whereArgs: MutableList<String> = ArrayList()
            for (folder in results) {
                ids.add(folder.id)
                likeList.add(LIKE_STATEMENT)
            }
            val WHERE = StringUtils.join(likeList, " OR ") + " COLLATE NOCASE"
            for (i in results.indices) {
                whereArgs.add(likeParam(ids[i]))
            }
            val WHERE_ARGS = whereArgs.toTypedArray()
            return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                    WHERE, WHERE_ARGS, null)
        }
        return null
    }

    override val searchCategory: MediaItemType?
        get() = MediaItemType.FOLDERS

    override val idPrefix: String
        get() = mediaItemTypeIds.getId(MediaItemType.FOLDER)!!

    override val projection: Array<String?>
        get() = Projections.FOLDER_PROJECTION.toTypedArray()

    @VisibleForTesting
    fun likeParam(value: String): String {
        return "%$value%"
    }

    companion object {
        private const val LIKE_STATEMENT = MediaStore.Audio.Media.DATA + " LIKE ?"
    }

}