package com.github.goldy1992.mp3player.service.library.content.parser

import android.database.Cursor
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
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
        while (cursor != null && cursor.moveToNext()) {
            val index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            if (index >= 0) {
                val path = cursor.getString(index)
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
            } else {
                Log.e(logTag(), "Could not find column index")
            }
        }
        return ArrayList(listToReturn)
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

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "Flder_Res_Prser"
    }

    private fun buildLibraryId(prefix: String, childItemId: String): String {
        return StringBuilder()
                .append(prefix)
                .append(ID_SEPARATOR)
                .append(childItemId)
                .toString()
    }
}