package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.commons.collections4.CollectionUtils
import javax.inject.Inject

class SongFromUriRetriever @Inject constructor(@ApplicationContext private val context: Context,
                                               private val contentResolver: ContentResolver,
                                               private val songResultsParser: SongResultsParser,
                                               private val mmr: MediaMetadataRetriever,
                                               mediaItemTypeIds: MediaItemTypeIds) {
    private val idPrefix: String?
    fun getSong(uri: Uri?): MediaItem? {
        if (uri != null && uri.scheme != null) {
            if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
                mmr.setDataSource(context, uri)
                val mediaItemBuilder = MediaItemBuilder("1")
                mediaItemBuilder.setMediaUri(uri)
                mediaItemBuilder.setAlbumArtImage(mmr.embeddedPicture)
                mediaItemBuilder.setTitle(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE))
                mediaItemBuilder.setArtist(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST))
                mediaItemBuilder.setDuration(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong())
                mmr.release()
                return mediaItemBuilder.build()
            } else {
                val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        PROJECTION, null, null, null)
                if (null != cursor) {
                    val results = songResultsParser.create(cursor)
                    if (CollectionUtils.isNotEmpty(results)) {
                        return results[0]
                    }
                }
            }
        }
        return null
    }

    companion object {
        private val PROJECTION: Array<String> = SONG_PROJECTION.toTypedArray()
    }

    init {
        idPrefix = mediaItemTypeIds.getId(MediaItemType.SONGS)
    }
}