package com.github.goldy1992.mp3player.service.library.content.observers

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.media3.session.MediaLibraryService.LibraryParams
import com.github.goldy1992.mp3player.commons.IoDispatcher
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.data.search.managers.SearchDatabaseManagers
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.exception.ExceptionUtils
import javax.inject.Inject

/**
 * A listener to the android ContentRetriever from the URI: content://media/external/audio/media or the
 * i.e. MediaStore.Audio.Media.EXTERNAL_CONTENT_URI constant.
 *
 * onChange is called when there is a change when a song indexed with the EXTERNAL_CONTENT_URI is
 * added, changed or deleted, and update respective song and folder lists
 */
@ServiceScoped
class AudioObserver
/**
 * Creates a content observer.
 * @param contentResolver The [ContentResolver]
 * @param contentManager The [ContentManager]
 * @param searchDatabaseManagers The [SearchDatabaseManagers]
 * @param ioDispatcher The [CoroutineDispatcher] of type [IoDispatcher]
 * @param mediaItemTypeIds The [MediaItemTypeIds]
 */
@Inject
constructor(
    contentResolver: ContentResolver,
    private val contentManager: ContentManager,
    private val searchDatabaseManagers: SearchDatabaseManagers,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    mediaItemTypeIds: MediaItemTypeIds) : MediaStoreObserver(contentResolver, mediaItemTypeIds) {

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
    override fun onChange(selfChange: Boolean, uri: Uri?, userId: Int) {
        Log.v(LOG_TAG, "onChange() invoked with uri: ${uri?.path ?: "null"}")
        if (startsWithUri(uri)) {
                runBlocking(ioDispatcher) {
                    updateSearchDatabase(uri)
                    mediaSession?.notifyChildrenChanged(mediaItemTypeIds.getId(MediaItemType.SONGS), 1, LibraryParams.Builder().build())
                    mediaSession?.notifyChildrenChanged(mediaItemTypeIds.getId(MediaItemType.FOLDERS), 1, LibraryParams.Builder().build())
                }
            }
            // when there is a "change" to the meta data the exact id will given as the uri


    }

    private suspend fun updateSearchDatabase(uri: Uri?) {
        var id = INVALID_ID
        try {
            id = ContentUris.parseId(uri!!)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ExceptionUtils.getStackTrace(ex))
        }
        // If we know the id then just get that id
        if (INVALID_ID != id) {
            val result = contentManager.getContentById(id.toString())
            Log.v(LOG_TAG, "updateSearchDatabase() UPDATING songs and folders index")
            searchDatabaseManagers.getSongDatabaseManager().insert(result)
            searchDatabaseManagers.getFolderDatabaseManager().insert(result)
            Log.v(LOG_TAG, "updateSearchDatabase() UPDATED songs and folders")
            val directoryPath = getDirectoryPath(result)
            if (StringUtils.isNotEmpty(directoryPath)) {
                mediaSession!!.notifyChildrenChanged(directoryPath, 1, LibraryParams.Builder().build())
            }
        } else {
            searchDatabaseManagers.getSongDatabaseManager().reindex()
            searchDatabaseManagers.getSongDatabaseManager().reindex()
        }
    }


    override val uri: Uri
        get() = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    companion object {
        const val LOG_TAG = "AudioObserver"
        private const val INVALID_ID: Long = -1
    }

}