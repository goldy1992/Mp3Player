package com.github.goldy1992.mp3player.service.library.content.observers

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.managers.FolderDatabaseManager
import com.github.goldy1992.mp3player.service.library.search.managers.SongDatabaseManager
import kotlinx.coroutines.*
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.exception.ExceptionUtils
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A listener to the android ContentRetriever from the URI: content://media/external/audio/media or the
 * i.e. MediaStore.Audio.Media.EXTERNAL_CONTENT_URI constant.
 *
 * onChange is called when there is a change when a song indexed with the EXTERNAL_CONTENT_URI is
 * added, changed or deleted, and update respective song and folder lists
 */
@Singleton
class AudioObserver
/**
 * Creates a content observer.
 */
@Inject constructor(contentResolver: ContentResolver,
                        /** Content manager  */
                        private val contentManager: ContentManager,
                        /** Search Database Manager  */
                        private val songDatabaseManager: SongDatabaseManager,
                        /** Search Database Manager  */
                        private val folderDatabaseManager: FolderDatabaseManager,
                        mediaItemTypeIds: MediaItemTypeIds) : MediaStoreObserver(contentResolver, mediaItemTypeIds), LogTagger {

    /** {@inheritDoc}  */
    override fun onChange(selfChange: Boolean) {
        onChange(selfChange, null)
    }

    /** {@inheritDoc}  */
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        onChange(selfChange, uri, -1)
    }

    /**
     * {@inheritDoc}
     *
     * For the purpose of the Normalised search database we're not interested in who made the change
     * therefore parameter, selfChange and userId are not used.
     *
     * @param selfChange not used
     * @param uri the uri that has changed
     * @param userId not used
     */
    @Suppress("UNUSED_PARAMETER")
    fun onChange(selfChange: Boolean, uri: Uri?, userId: Int) {
        if (startsWithUri(uri)) {
                runBlocking {
                    updateSearchDatabase(uri)
                    mediaPlaybackService!!.notifyChildrenChanged(mediaItemTypeIds.getId(MediaItemType.SONGS)!!)
                    mediaPlaybackService!!.notifyChildrenChanged(mediaItemTypeIds.getId(MediaItemType.FOLDERS)!!)
                }
            }
            // when there is a "change" to the meta data the exact id will given as the uri
            Log.i(logTag(), "hit on change")

    }

    private suspend fun updateSearchDatabase(uri: Uri?) {
        var id = INVALID_ID
        try {
            id = ContentUris.parseId(uri)
        } catch (ex: Exception) {
            Log.e(logTag(), ExceptionUtils.getStackTrace(ex))
        }
        // If we know the id then just get that id
        if (INVALID_ID != id) {
            val result = contentManager.getItem(id)
            if (null != result) {
                Log.i(logTag(), "UPDATING songs and folders index")
                songDatabaseManager.insert(result)
                folderDatabaseManager.insert(result)
                Log.i(logTag(), "UPDATED songs and folders")
                val directoryPath = getDirectoryPath(result)
                if (StringUtils.isNotEmpty(directoryPath)) {
                    mediaPlaybackService!!.notifyChildrenChanged(directoryPath!!)
                }
            }
        } else {
               withContext(Dispatchers.IO) {
                    songDatabaseManager.reindex()
                    folderDatabaseManager.reindex()
                }

        }
    }

    override fun logTag(): String {
        return "AUDIO_OBSERVER"
    }

    override val uri: Uri
        get() = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    companion object {
        private const val INVALID_ID: Long = -1
    }

}