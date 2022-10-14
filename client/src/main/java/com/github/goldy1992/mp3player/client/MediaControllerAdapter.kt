package com.github.goldy1992.mp3player.client

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import com.github.goldy1992.mp3player.commons.Constants.CHANGE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.commons.DefaultDispatcher
import com.github.goldy1992.mp3player.commons.IoDispatcher
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * An adapter that can be used to control the MediaPlaybackService by sending playback controls such
 * as play/pause to the service.
 */
@ActivityRetainedScoped
open class MediaControllerAdapter
    @Inject
    constructor(
        private val mediaControllerFuture: ListenableFuture<MediaController>,
        private val scope : CoroutineScope,
        @IoDispatcher private val ioDispatcher : CoroutineDispatcher,
        @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
        @DefaultDispatcher val defaultDispatcher: CoroutineDispatcher)
        : MediaController.Listener, LogTagger {

    private var mediaController: MediaController? = null

    init {
        scope.launch {
            mediaController = mediaControllerFuture.await()
        }
    }

    open fun getCurrentQueuePosition() : Int {
        return mediaController?.currentMediaItemIndex ?: 0
    }

    open suspend fun prepareFromMediaId(mediaItem: MediaItem, extras: Bundle?) {
        mediaControllerFuture.await().addMediaItem(mediaItem)
        // call from application looper
        scope.launch(mainDispatcher) {
            mediaControllerFuture.await().prepare()
        }
    }

    open fun playFromMediaId(mediaId: String, extras: Bundle?) {
        val mediaItem = MediaItem.Builder().setMediaId(mediaId).build()
        mediaController?.addMediaItem(mediaItem)
    }

    open suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        val mediaItem = MediaItem.Builder().setUri(uri).build()
        mediaController?.addMediaItem(mediaItem)

    }

    open suspend fun play() {
        val future = mediaControllerFuture.await()
        Log.i(logTag(), "awaiting future for play")
        future.play()
        Log.i(logTag(), "calling play")
    }

    open suspend fun pause() { //Log.i(LOG_TAG, "pause hit");
        mediaControllerFuture.await().pause()
    }

    open suspend fun seekTo(position: Long) {
        mediaControllerFuture.await().seekTo(position)
    }

    open suspend fun stop() {
        mediaControllerFuture.await().stop()
    }

    open suspend fun skipToNext() {
        mediaControllerFuture.await().seekToNextMediaItem()
    }

    open suspend fun skipToPrevious() {
        mediaControllerFuture.await().seekToPrevious()
    }

    open suspend fun setShuffleMode(shuffleModeEnabled : Boolean) {
        mediaControllerFuture.await().shuffleModeEnabled = shuffleModeEnabled
    }

    open suspend fun setRepeatMode(@Player.RepeatMode repeatMode: Int) {
        mediaControllerFuture.await().repeatMode = repeatMode
    }

    open suspend fun sendCustomAction(customAction: String, args: Bundle) {
        val sessionCommand = SessionCommand(customAction, args)
        mediaControllerFuture.await().sendCustomCommand(sessionCommand, args).await()
    }

    open suspend fun changePlaybackSpeed(speed: Float) {
        val extras = Bundle()
        extras.putFloat(CHANGE_PLAYBACK_SPEED, speed)
        val changePlaybackSpeedCommand = SessionCommand(CHANGE_PLAYBACK_SPEED, extras)
        mediaControllerFuture.await().sendCustomCommand(changePlaybackSpeedCommand, extras)?.await()
    }

    open fun getCurrentPlaybackPosition() : Long {
        return mediaController?.currentPosition ?: 0L
    }

    open fun getCurrentMediaItem() : MediaItem {
        return mediaController?.currentMediaItem ?: MediaItem.EMPTY
    }

    override fun logTag(): String {
        return "MDIA_CNTRLLR_ADPTR"
    }
//    open fun calculateCurrentQueuePosition(): Int {
//        val currentQueue = queue.value
//        val activeQueueItemId = getActiveQueueItemId()
//        if (currentQueue != null) {
//            for (i in currentQueue.indices) {
//                val queueItem = currentQueue[i]
//                if (queueItem.queueId == activeQueueItemId) {
//                    return i
//                }
//            }
//        }
//        return -1
//    }
//    open fun getCurrentSongAlbumArtUri() : Uri? {
//        val albumArtUriPath = metadata.value!!.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)
//
//        return try {
//            Uri.parse(albumArtUriPath)
//        } catch (ex: NullPointerException) {
//            Log.e(logTag(), "$albumArtUriPath: is an invalid Uri")
//            return null
//        }
//    }

}
