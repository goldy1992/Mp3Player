package com.github.goldy1992.mp3player.client

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ShuffleMode
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import org.apache.commons.lang3.exception.ExceptionUtils
import javax.inject.Inject

@ComponentScope
open class MediaControllerAdapter
@Inject
constructor(private val context: Context,
            private val myMediaControllerCallback: MyMediaControllerCallback?) {


    @get:VisibleForTesting
    @set:VisibleForTesting
    var mediaController: MediaControllerCompat? = null

    open var token: MediaSessionCompat.Token? = null

    open fun setMediaToken(token: MediaSessionCompat.Token?) {
        if (!isInitialized && token != null) {
            init(token)
        } else {
            Log.e(LOG_TAG, "MediaControllerAdapter already initialised")
        }
    }

    @VisibleForTesting
    fun init(token: MediaSessionCompat.Token) {
        try {
            mediaController = MediaControllerCompat(context, token)
            mediaController!!.registerCallback(myMediaControllerCallback!!)
        } catch (ex: RemoteException) {
            Log.e(LOG_TAG, ExceptionUtils.getStackTrace(ex))
        }
        this.token = token
    }

    open fun prepareFromMediaId(mediaId: String?, extras: Bundle?) {
        controller.prepareFromMediaId(mediaId, extras)
    }

    open fun playFromMediaId(mediaId: String?, extras: Bundle?) {
        controller.playFromMediaId(mediaId, extras)
    }

    open fun playFromUri(uri: Uri?, extras: Bundle?) {
        controller.playFromUri(uri, extras)
    }

    open fun play() {
        controller.play()
    }

    open fun pause() { //Log.i(LOG_TAG, "pause hit");
        controller.pause()
    }

    open fun seekTo(position: Long) {
        controller.seekTo(position)
    }

    open fun stop() {
        controller.stop()
    }

    open fun skipToNext() {
        controller.skipToNext()
    }

    open fun skipToPrevious() {
        controller.skipToPrevious()
    }

    open fun registerMetaDataListener(metaDataListener: MetadataListener?) {
        myMediaControllerCallback!!.myMetaDataCallback.registerMetaDataListener(metaDataListener!!)
    }

    open fun unregisterMetaDataListener(metaDataListener: MetadataListener?) {
        myMediaControllerCallback!!.myMetaDataCallback.removeMetaDataListener(metaDataListener)
    }

    open fun registerPlaybackStateListener(playbackStateListener: PlaybackStateListener?) {
        myMediaControllerCallback!!.myPlaybackStateCallback.registerPlaybackStateListener(playbackStateListener!!)
    }

    fun unregisterPlaybackStateListener(playbackStateListener: PlaybackStateListener?) {
        myMediaControllerCallback!!.myPlaybackStateCallback.removePlaybackStateListener(playbackStateListener)
    }

    open val playbackState: Int
        get() {
            val playbackStateCompat = playbackStateCompat
            return playbackStateCompat?.state ?: 0
        }

    open val playbackStateCompat: PlaybackStateCompat?
        get() = if (mediaController != null) {
            mediaController!!.playbackState
        } else null

    open val metadata: MediaMetadataCompat?
        get() = if (mediaController != null) {
            mediaController!!.metadata
        } else null

    @get:ShuffleMode
    open var shuffleMode: Int
        get() = mediaController!!.shuffleMode
        set(shuffleMode) {
            controller.setShuffleMode(shuffleMode)
        }

    @get:PlaybackStateCompat.RepeatMode
    open var repeatMode: Int
        get() = mediaController!!.repeatMode
        set(repeatMode) {
            controller.setRepeatMode(repeatMode)
        }

    val currentSongAlbumArtUri: Uri?
        get() {
            val currentMetaData = metadata
            val albumArtUriPath = currentMetaData!!.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)

            return try {
                Uri.parse(albumArtUriPath)
            } catch (ex: NullPointerException) {
                Log.e(LOG_TAG, "$albumArtUriPath: is an invalid Uri")
                return null
            }
        }

    open fun disconnect() {
        if (mediaController != null && myMediaControllerCallback != null) {
            mediaController!!.unregisterCallback(myMediaControllerCallback)
        }
    }

    open val isInitialized: Boolean
        get() = mediaController != null && mediaController!!.isSessionReady

    open fun sendCustomAction(customAction: String?, args: Bundle?) {
        controller.sendCustomAction(customAction, args)
    }

    @get:VisibleForTesting
    val controller: MediaControllerCompat.TransportControls
        get() = mediaController!!.transportControls

    open val queue: List<MediaSessionCompat.QueueItem>?
        get() = mediaController!!.queue

    open val activeQueueItemId: Long
        get() = mediaController!!.playbackState.activeQueueItemId

    val currentQueuePosition: Int
        get() {
            val queue = queue
            if (queue != null) {
                val id = activeQueueItemId
                for (i in queue.indices) {
                    val queueItem = queue[i]
                    if (queueItem.queueId == id) {
                        return i
                    }
                }
            }
            return -1
        }

    companion object {
        private const val LOG_TAG = "MDIA_CNTRLLR_ADPTR"
    }

}