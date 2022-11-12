package com.github.goldy1992.mp3player.client

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.github.goldy1992.mp3player.commons.Constants.CHANGE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.commons.DefaultDispatcher
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * An adapter that can be used to control the MediaPlaybackService by sending playback controls such
 * as play/pause to the service.
 */
@ActivityRetainedScoped
open class MediaControllerAdapter
    @Inject
    constructor(
        val mediaControllerFuture: ListenableFuture<MediaController>,
        private val scope : CoroutineScope,
        @MainDispatcher private val mainDispatcher : CoroutineDispatcher)
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

    open suspend fun getCurrentQueuePositionAsync() : Int {
        return mediaControllerFuture.await().currentMediaItemIndex
    }

    open suspend fun prepareFromMediaId(mediaItem: MediaItem) {

        // call from application looper
        scope.launch(mainDispatcher) {
            val mediaController = mediaControllerFuture.await()
            mediaController.addMediaItem(mediaItem)
            mediaController.prepare()
        }
    }

    open fun playFromMediaId(mediaItem : MediaItem) {
        scope.launch(mainDispatcher) {
            val mediaController = mediaControllerFuture.await()
            mediaController.addMediaItem(mediaItem)
            mediaController.prepare()
            mediaController.play()
        }
    }

    override fun onCustomCommand(
        controller: MediaController,
        command: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        return super.onCustomCommand(controller, command, args)
    }

    open fun playFromSongList(itemIndex : Int, items : List<MediaItem>) {
        scope.launch(mainDispatcher) {
            val mediaController = mediaControllerFuture.await()
            mediaController.clearMediaItems()
            mediaController.addMediaItems(items)
            mediaController.seekTo(itemIndex, 0L)
            mediaController.prepare()
            mediaController.play()
        }
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
        mediaControllerFuture.await().seekToPreviousMediaItem()
    }

    open suspend fun setShuffleMode(shuffleModeEnabled : Boolean) {
        mediaControllerFuture.await().shuffleModeEnabled = shuffleModeEnabled
    }

    open suspend fun setRepeatMode(@Player.RepeatMode repeatMode: Int) {
        mediaControllerFuture.await().repeatMode = repeatMode
    }

    open suspend fun sendCustomAction(customAction: String, args: Bundle) {
        val sessionCommand = SessionCommand(customAction, args)
        mediaControllerFuture.await().sendCustomCommand(sessionCommand, args)
    }

    open suspend fun changePlaybackSpeed(speed: Float) {
        val extras = Bundle()
        extras.putFloat(CHANGE_PLAYBACK_SPEED, speed)
        val changePlaybackSpeedCommand = SessionCommand(CHANGE_PLAYBACK_SPEED, extras)
        mediaControllerFuture.await().sendCustomCommand(changePlaybackSpeedCommand, extras).await()
    }

    open fun getCurrentPlaybackPosition() : Long {
        return mediaController?.currentPosition ?: 0L
    }

    open suspend fun getCurrentMediaItem() : MediaItem {
        return mediaControllerFuture.await().currentMediaItem ?: MediaItem.EMPTY
    }

    override fun logTag(): String {
        return "MDIA_CNTRLLR_ADPTR"
    }

}
