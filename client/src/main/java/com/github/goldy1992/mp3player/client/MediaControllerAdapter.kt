package com.github.goldy1992.mp3player.client

import android.content.Context
import android.media.session.PlaybackState
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
import com.github.goldy1992.mp3player.commons.Constants.CHANGE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.commons.LogTagger
import org.apache.commons.lang3.exception.ExceptionUtils

/**
 * An adapter that can be used to control the MediaPlaybackService by sending playback controls such
 * as play/pause to the service.
 */
open class MediaControllerAdapter

constructor(private val context: Context,
            private var mediaBrowser: MediaBrowserCompat)
    : MediaBrowserConnectionListener, LogTagger, MediaControllerCompat.Callback() {


    private var mediaController: MediaControllerCompat? = null

    open var token: MediaSessionCompat.Token? = null

    open val metadata : MutableLiveData<MediaMetadataCompat> = MutableLiveData()

    open val playbackState : MutableLiveData<PlaybackStateCompat> = MutableLiveData()

    open val queue : MutableLiveData<MutableList<MediaSessionCompat.QueueItem>> = MutableLiveData()

    open val repeatMode : MutableLiveData<Int> = MutableLiveData()

    open val shuffleMode : MutableLiveData<Int> = MutableLiveData()

    open val playbackSpeed : MutableLiveData<Float> = MutableLiveData(1f)

    /**
     * @return True if the mediaBrowser is connected
     */
    open fun isConnected() : Boolean {
        return mediaBrowser != null && mediaBrowser.isConnected
    }

    open fun prepareFromMediaId(mediaId: String?, extras: Bundle?) {
        if (isConnected()) {
            transportControls.prepareFromMediaId(mediaId, extras)
        }
    }

    open fun playFromMediaId(mediaId: String?, extras: Bundle?) {
        transportControls.playFromMediaId(mediaId, extras)
    }

    open suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
         transportControls.playFromUri(uri, extras)
    }

    open fun play() {
        if (isConnected()) {
            transportControls.play()
        }
    }

    open fun pause() { //Log.i(LOG_TAG, "pause hit");
        if (isConnected()) {
            transportControls.pause()
        }
    }

    open fun seekTo(position: Long) {
        if (isConnected()) {
            transportControls.seekTo(position)
        }
    }

    open fun stop() {
        if (isConnected()) {
            transportControls.stop()
        }
    }

    open fun skipToNext() {
        if (isConnected()) {
            transportControls.skipToNext()
        }
    }

    open fun skipToPrevious() {
        if (isConnected()) {
            transportControls.skipToPrevious()
        }
    }

    open fun setShuffleMode(shuffleMode: Int) {
        if (isConnected()) {
            transportControls.setShuffleMode(shuffleMode)
        }
    }

    open fun setRepeatMode(repeatMode: Int) {
        if (isConnected()) {
            transportControls.setRepeatMode(repeatMode)
        }
    }

    open fun disconnect() {
        if (mediaController != null) {
            mediaController!!.unregisterCallback(this)
        }
    }

    open fun sendCustomAction(customAction: String?, args: Bundle?) {
        transportControls.sendCustomAction(customAction, args)
    }

    open fun changePlaybackSpeed(speed: Float) {
        val extras = Bundle()
        extras.putFloat(CHANGE_PLAYBACK_SPEED, speed)
        transportControls.sendCustomAction(CHANGE_PLAYBACK_SPEED, extras)
    }

    @get:VisibleForTesting
    val transportControls: MediaControllerCompat.TransportControls
    get() = mediaController!!.transportControls

    open fun getActiveQueueItemId(): Long? {
        return playbackState.value?.activeQueueItemId
    }

    open fun calculateCurrentQueuePosition(): Int {
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

    open fun createMediaController(
        context: Context,
        token: MediaSessionCompat.Token
    ): MediaControllerCompat {
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
        val playing = state.state == PlaybackStateCompat.STATE_PLAYING
        this.isPlaying.postValue(playing)
        Log.i(logTag(), "IS PLAYING: $playing")
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

    open fun getCurrentSongAlbumArtUri() : Uri? {
        val albumArtUriPath = metadata.value!!.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)

        return try {
            Uri.parse(albumArtUriPath)
        } catch (ex: NullPointerException) {
            Log.e(logTag(), "$albumArtUriPath: is an invalid Uri")
            return null
        }
    }
    /**
     * @return True if the current playback state is [PlaybackStateCompat.STATE_PLAYING].
     */
    val isPlaying = MutableLiveData<Boolean>(false)

    override fun onConnected() {
        try {
            this.token = mediaBrowser.sessionToken
            this.mediaController = createMediaController(context, mediaBrowser.sessionToken)
            this.mediaController!!.registerCallback(this)
            metadata.postValue(mediaController!!.metadata)
            playbackState.postValue(mediaController!!.playbackState)
            queue.postValue(mediaController!!.queue)
            //isPlaying.postValue((mediaController!!.playbackState?.playbackState as PlaybackState).state == PlaybackStateCompat.STATE_PLAYING)
        } catch (ex: RemoteException) {
            Log.e(logTag(), ExceptionUtils.getStackTrace(ex))
        }
    }
}
