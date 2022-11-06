package com.github.goldy1992.mp3player.client

import android.content.Context
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.session.MediaBrowser
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.apache.commons.lang3.exception.ExceptionUtils

@ActivityRetainedScoped
class AudioDataAdapter

    (context: Context,
     mediaBrowser: MediaBrowser,
     private var scope : CoroutineScope
) : MediaBrowserConnectionListener,
    LogTagger, DefaultLifecycleObserver, MediaAdapter(context, mediaBrowser) {

    var isScreenVisible = false

    val isPlayingFlow : Flow<PlaybackStateCompat> = callbackFlow {
        val messageListener = object : MediaControllerCallbackImpl() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                if (state?.state == PlaybackStateCompat.STATE_PLAYING) {
                    trySend(state)
                }
            }
        }

        mediaController?.registerCallback(messageListener)


        // The callback inside awaitClose will be executed when the flow is
        // either closed or cancelled.
        // In this case, remove the callback from Firestore
        awaitClose { mediaController?.unregisterCallback(messageListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    val isPlaying = MutableStateFlow(false)

    init {
        scope.launch {
            isPlayingFlow.collect {
               isPlaying.value = it.state == PlaybackStateCompat.STATE_PLAYING
            }
        }
    }


    val audioDataFlow : Flow<AudioSample> = callbackFlow<AudioSample> {

        val messagesListener = object : MediaControllerCallbackImpl() {
            override fun onSessionEvent(event: String?, extras: Bundle?) {
                super.onSessionEvent(event, extras)
                if (isScreenVisible && (Constants.AUDIO_DATA == event) && isPlaying.value) {

                    val audioSample = extras?.get(Constants.AUDIO_DATA) as AudioSample
                    trySend(audioSample)
                    //      Log.i(logTag(), "sending audio sample, result: ${result.isSuccess}")
                }

            }
        }

        mediaController?.registerCallback(messagesListener)


        // The callback inside awaitClose will be executed when the flow is
        // either closed or cancelled.
        // In this case, remove the callback from Firestore
        awaitClose { mediaController?.unregisterCallback(messagesListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    override fun logTag(): String {
        return "AudioDataAdapter"
    }


    override fun onResume(owner: LifecycleOwner) {
        isScreenVisible = true
    }

    override fun onPause(owner: LifecycleOwner) {
        isScreenVisible = false
    }

}