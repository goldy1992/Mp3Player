package com.github.goldy1992.mp3player.service.library.content.parser

import android.database.Cursor
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat.MediaItem
import com.github.goldy1992.mp3player.commons.ComparatorUtils
import com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath
import java.io.File
import java.util.*
import javax.inject.Inject

class FolderResultsParser

    @Inject
    constructor() : ResultsParser() {

    override fun create(cursor: Cursor?, mediaIdPrefix: String?): List<MediaItem> {
        val listToReturn = TreeSet(this)
        val directoryPathSet: MutableSet<String> = HashSet()
        while (cursor!!.moveToNext()) {
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            val file = File(path)
            if (file.exists()) {
                val directory = file.parentFile
                var directoryPath: String
                if (null != directory) {
                    directoryPath = directory.absolutePath
                    if (directoryPathSet.add(directoryPath)) {
                        val mediaItem = createFolderMediaItem(directory, mediaIdPrefix!!)
                        listToReturn.add(mediaItem)
                    }
                }
            }
        }
        return ArrayList(listToReturn) as List<MediaItem>
    }

    override val type: MediaItemType?
        get() = MediaItemType.FOLDER

    private fun createFolderMediaItem(folder: File, parentId: String): MediaItem { /* append a file separator so that folders with an "extended" name are discarded...
         * e.g. Folder to accept: 'folder1'
         *      Folder to reject: 'folder1extended' */
        val filePath = folder.absolutePath + File.separator
        return MediaItemBuilder(filePath)
                .setMediaItemType(MediaItemType.FOLDER)
                .setLibraryId(buildLibraryId(parentId, filePath))
                .setDirectoryFile(folder)
                .setFlags(MediaItem.FLAG_BROWSABLE)
                .build()
    }

    override fun compare(m1: MediaItem, m2: MediaItem): Int {
        return ComparatorUtils.Companion.caseSensitiveStringCompare.compare(getDirectoryPath(m1), getDirectoryPath(m2))
    }

    private fun buildLibraryId(prefix: String, childItemId: String): String {
        return StringBuilder()
                .append(prefix)
                .append(ID_SEPARATOR)
                .append(childItemId)
                .toString()
    }
}