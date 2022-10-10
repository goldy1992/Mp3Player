package com.github.goldy1992.mp3player.service.library.content.parser

import android.database.Cursor
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import android.util.Log
import androidx.media3.common.FlagSet
import androidx.media3.common.MediaMetadata.FOLDER_TYPE_MIXED
import com.github.goldy1992.mp3player.commons.ComparatorUtils
import com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.collections.HashMap

class FolderResultsParser

    @Inject
    constructor() : ResultsParser() {

    override fun create(cursor: Cursor?, mediaIdPrefix: String?): List<MediaItem> {
        val listToReturn = TreeSet(this)
        val directoryPathMap : MutableMap<String, DirectoryInfo> = HashMap()
        while (cursor != null && cursor.moveToNext()) {
            val index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            if (index >= 0) {
                val path = cursor.getString(index)
                val file = File(path)
                if (file.exists()) {
                    val directory = file.parentFile
                    if (null != directory) {
                        val directoryPath = directory.absolutePath
                        if (directoryPathMap.containsKey(directoryPath)) {
                            directoryPathMap[directoryPath]?.fileCount?.incrementAndGet()
                        } else {
                            directoryPathMap[directoryPath] = DirectoryInfo(directory)
                        }
                    }
                }
            } else {
                Log.e(logTag(), "Could not find column index")
            }
        }

        directoryPathMap.entries.forEach {
            val mediaItem = createFolderMediaItem(it.value, mediaIdPrefix)
            listToReturn.add(mediaItem)
        }
        return ArrayList(listToReturn)
    }

    override fun create(cursor: Cursor): List<MediaItem> {
        return create(cursor, null)
    }

    override val type: MediaItemType?
        get() = MediaItemType.FOLDER

    private fun createFolderMediaItem(directoryInfo: DirectoryInfo, parentId: String?) : MediaItem { /* append a file separator so that folders with an "extended" name are discarded...
         * e.g. Folder to accept: 'folder1'
         *      Folder to reject: 'folder1extended' */
        val folder = directoryInfo.directory
        val filePath = folder.absolutePath + File.separator
        return MediaItemBuilder(filePath)
            .setMediaItemType(MediaItemType.FOLDER)
            .setLibraryId(buildLibraryId(parentId ?: "null", filePath))
            .setDirectoryFile(folder)
            .setFileCount(directoryInfo.fileCount.get())
            .setFolderType(FOLDER_TYPE_MIXED)
            .setIsPlayable(false)
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

    /**
     * Used to store the Directory Information along with the number of files in the directory,
     * which is incremented every time a file is found.
     */
    private data class DirectoryInfo(val directory : File) {
        /**
         * Assumes the object is created when the first occurrence of a file in the directory is
         * found.
         */
        val fileCount : AtomicInteger = AtomicInteger(1)
    }
}