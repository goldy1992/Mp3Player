package com.github.goldy1992.mp3player.service.player

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.ContentDataSource
import com.google.android.exoplayer2.upstream.ContentDataSource.ContentDataSourceException
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.FileDataSource.FileDataSourceException
import org.apache.commons.lang3.exception.ExceptionUtils
import javax.inject.Inject

class MediaSourceFactory @Inject constructor(private val fileDataSource: FileDataSource,
                                             private val contentDataSource: ContentDataSource) : LogTagger {
    fun createMediaSource(uri: Uri?): MediaSource? {
        val dataSpec = DataSpec(uri)
        val dataSrcFactory = openDataSpec(dataSpec) ?: return null
        val factory = ProgressiveMediaSource.Factory(dataSrcFactory)
        return factory.createMediaSource(uri)
    }

    private fun openDataSpec(dataSpec: DataSpec): MyDataSourceFactory? {
        val dataSpecUri = dataSpec.uri
        val scheme = dataSpecUri.scheme
        return try {
            if (ContentResolver.SCHEME_FILE == scheme) {
                fileDataSource.open(dataSpec)
                MyDataSourceFactory(fileDataSource)
            } else {
                contentDataSource.open(dataSpec)
                MyDataSourceFactory(contentDataSource)
            }
        } catch (ex: ContentDataSourceException) {
            Log.e(logTag(), ExceptionUtils.getStackTrace(ex))
            null
        } catch (ex: FileDataSourceException) {
            Log.e(logTag(), ExceptionUtils.getStackTrace(ex))
            null
        }
    }

    override fun logTag(): String {
        return "MDIA_SRC_FACTORY"
    }

}