package com.github.goldy1992.mp3player.client

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ShuffleMode
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.commons.LogTagger
import org.apache.commons.lang3.exception.ExceptionUtils

open class MediaControllerAdapter

constructor(private val context: Context,
            private var mediaBrowserCompat: MediaBrowserCompat)
    : MediaBrowserConnectionListener, LogTagger, MediaControllerCompat.Callback() {

    @get:VisibleForTesting
    @set:VisibleForTesting
    var mediaController: MediaControllerCompat? = null

    open var token: MediaSessionCompat.Token? = null

    val metadata : MutableLiveData<MediaMetadataCompat> = MutableLiveData()

    val playbackState : MutableLiveData<PlaybackStateCompat> = MutableLiveData()

    val queue : MutableLiveData<MutableList<MediaSessionCompat.QueueItem>> = MutableLiveData()

    override fun onConnected() {
        try {
            this.token = mediaBrowserCompat.sessionToken
            mediaController = createMediaController(context, mediaBrowserCompat.sessionToken)
            mediaController!!.registerCallback(this)
            metadata.postValue(mediaController!!.metadata)
            playbackState.postValue(mediaController!!.playbackState)
            queue.postValue(mediaController!!.queue)
        } catch (ex: RemoteException) {
            Log.e(logTag(), ExceptionUtils.getStackTrace(ex))
        }
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

    @ShuffleMode
    open fun getShuffleMode() : Int? {
       return mediaController!!.shuffleMode
    }

    open fun setShuffleMode(shuffleMode : Int) {
        controller.setShuffleMode(shuffleMode)
    }

    @PlaybackStateCompat.RepeatMode
    open fun getRepeatMode() : Int? {
        return mediaController?.repeatMode
    }

    open fun setRepeatMode(repeatMode : Int) {
        controller.setRepeatMode(repeatMode)
    }

    open fun disconnect() {
        if (mediaController != null) {
            mediaController!!.unregisterCallback(this)
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

    open fun getQueue(): List<MediaSessionCompat.QueueItem>? {
        return mediaController!!.queue
    }

    open fun getActiveQueueItemId(): Long? {
        return mediaController!!.playbackState.activeQueueItemId
    }

   open fun getCurrentQueuePosition() : Int {
        val queue = getQueue()
        if (queue != null) {
            val id = getActiveQueueItemId()
            for (i in queue.indices) {
                val queueItem = queue[i]
                if (queueItem.queueId == id) {
                    return i
                }
            }
        }
        return -1
    }

    open fun createMediaController(context: Context, token: MediaSessionCompat.Token) : MediaControllerCompat {
        return MediaControllerCompat(context, token)
    }

    override fun logTag(): String {
        return "MDIA_CNTRLLR_ADPTR"
    }

    override fun onMetadataChanged(metadata: MediaMetadataCompat) {
        this.metadata.postValue(metadata)
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        this.playbackState.postValue(state)
    }

    override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
        this.queue.postValue(queue!!)
    }


}