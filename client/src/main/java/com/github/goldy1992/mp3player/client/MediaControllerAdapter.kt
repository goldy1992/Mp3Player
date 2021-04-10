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
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apache.commons.lang3.exception.ExceptionUtils

/**
 * An adapter that can be used to control the MediaPlaybackService by sending playback controls such
 * as play/pause to the service.
 */
open class MediaControllerAdapter

constructor(private val context: Context,
            private var mediaBrowserCompat: MediaBrowserCompat)
    : MediaBrowserConnectionListener, LogTagger, MediaControllerCompat.Callback() {


    private val mutex : Mutex = Mutex()

    init {
        runBlocking { mutex.lock() }
    }

    private var mediaController: MediaControllerCompat? = null

    open var token: MediaSessionCompat.Token? = null

    open val metadata : MutableLiveData<MediaMetadataCompat> = MutableLiveData()

    open val playbackState : MutableLiveData<PlaybackStateCompat> = MutableLiveData()

    open val queue : MutableLiveData<MutableList<MediaSessionCompat.QueueItem>> = MutableLiveData()

    open val repeatMode : MutableLiveData<Int> = MutableLiveData()

    open val shuffleMode : MutableLiveData<Int> = MutableLiveData()

    open val playbackSpeed : MutableLiveData<Float> = MutableLiveData()

    override fun onConnected() {
        try {
            this.token = mediaBrowserCompat.sessionToken
            mediaController = createMediaController(context, mediaBrowserCompat.sessionToken)
            mediaController!!.registerCallback(this)
            metadata.postValue(mediaController!!.metadata)
            playbackState.postValue(mediaController!!.playbackState)
            queue.postValue(mediaController!!.queue)
            runBlocking { mutex.unlock() }
        } catch (ex: RemoteException) {
            Log.e(logTag(), ExceptionUtils.getStackTrace(ex))
        }
    }

    open fun prepareFromMediaId(mediaId: String?, extras: Bundle?) {
        transportControls.prepareFromMediaId(mediaId, extras)
    }

    open fun playFromMediaId(mediaId: String?, extras: Bundle?) {
        transportControls.playFromMediaId(mediaId, extras)
    }

    open suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        mutex.withLock {  transportControls.playFromUri(uri, extras) }
    }

    open fun play() {
        transportControls.play()
    }

    open fun pause() { //Log.i(LOG_TAG, "pause hit");
        transportControls.pause()
    }

    open fun seekTo(position: Long) {
        transportControls.seekTo(position)
    }

    open fun stop() {
        transportControls.stop()
    }

    open fun skipToNext() {
        transportControls.skipToNext()
    }

    open fun skipToPrevious() {
        transportControls.skipToPrevious()
    }

    open fun setShuffleMode(shuffleMode : Int) {
        transportControls.setShuffleMode(shuffleMode)
    }

    open fun setRepeatMode(repeatMode : Int) {
        transportControls.setRepeatMode(repeatMode)
    }

    open fun disconnect() {
        if (mediaController != null) {
            mediaController!!.unregisterCallback(this)
        }
    }

    open fun sendCustomAction(customAction: String?, args: Bundle?) {
        transportControls.sendCustomAction(customAction, args)
    }

    @get:VisibleForTesting
    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController!!.transportControls

    open fun getActiveQueueItemId(): Long? {
        return playbackState.value?.activeQueueItemId
    }

   open fun calculateCurrentQueuePosition() : Int {
       val currentQueue = queue.value
       val activeQueueItemId = getActiveQueueItemId()
        if (currentQueue != null) {
            for (i in currentQueue.indices) {
                val queueItem = currentQueue[i]
                if (queueItem.queueId == activeQueueItemId) {
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
        this.playbackSpeed.postValue(state.playbackSpeed)
    }

    override fun onQueueChanged(newQueue: MutableList<MediaSessionCompat.QueueItem>?) {
        this.queue.postValue(newQueue!!)
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        this.repeatMode.postValue(repeatMode)
    }

    override fun onShuffleModeChanged(shuffleMode: Int) {
        this.shuffleMode.postValue(shuffleMode)
    }

}