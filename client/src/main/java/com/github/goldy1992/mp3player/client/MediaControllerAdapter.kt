package com.github.goldy1992.mp3player.client

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.concurrent.futures.await
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import com.github.goldy1992.mp3player.commons.Constants.CHANGE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * An adapter that can be used to control the MediaPlaybackService by sending playback controls such
 * as play/pause to the service.
 */
@ActivityRetainedScoped
open class MediaControllerAdapter

constructor( mediaControllerFuture: ListenableFuture<MediaController>,
scope : CoroutineScope)
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

    open fun prepareFromMediaId(mediaId: String, extras: Bundle?) {
        val mediaItem = MediaItem.Builder().setMediaId(mediaId).build()
        mediaController?.addMediaItem(mediaItem)
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
        mediaController?.play()
    }

    open suspend fun pause() { //Log.i(LOG_TAG, "pause hit");
        mediaController?.pause()
    }

    open suspend fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    open suspend fun stop() {
        mediaController?.stop()
    }

    open suspend fun skipToNext() {
        mediaController?.seekToNextMediaItem()
    }

    open suspend fun skipToPrevious() {
        mediaController?.seekToPrevious()
    }

    open suspend fun setShuffleMode(shuffleModeEnabled : Boolean) {
        mediaController?.shuffleModeEnabled = shuffleModeEnabled
    }

    open suspend fun setRepeatMode(@Player.RepeatMode repeatMode: Int) {
        mediaController?.repeatMode = repeatMode
    }

    open suspend fun sendCustomAction(customAction: String, args: Bundle) {
        val sessionCommand = SessionCommand(customAction, args)
        mediaController?.sendCustomCommand(sessionCommand, args)?.await()
    }

    open suspend fun changePlaybackSpeed(speed: Float) {
        val extras = Bundle()
        extras.putFloat(CHANGE_PLAYBACK_SPEED, speed)
        val changePlaybackSpeedCommand = SessionCommand(CHANGE_PLAYBACK_SPEED, extras)
        mediaController?.sendCustomCommand(changePlaybackSpeedCommand, extras)?.await()
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

}
