package com.github.goldy1992.mp3player.service

import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.service.library.data.search.managers.SearchDatabaseManagersFullImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import androidx.annotation.OptIn as AndroidXOptIn

/**
 * Created by Mike on 24/09/2017.
 *
 * Implementation of the MediaBrowserService. This service will run in the foreground when the front
 * end of the application interacts with it. When the application is closed or removed from the task
 * list, the service will continue to run in the background and only be destroyed by the operating
 * system.
 */
@AndroidEntryPoint
class MediaPlaybackService : MediaLibraryService() {

    companion object {
        const val LOG_TAG = "MEDIA_PLAYBACK_SERVICE"
    }
    @Inject
    lateinit var componentClassMapper: ComponentClassMapper

    @Inject
    lateinit var mediaSessionCreator: MediaSessionCreator

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    private var mediaSession: MediaLibrarySession? = null

    @ServiceCoroutineScope
    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var searchDatabaseManagers: SearchDatabaseManagersFullImpl

    @Inject
    lateinit var playerStateManager: PlayerStateManager

    override fun onCreate() {
        Log.v(LOG_TAG, "onCreate() invoked.")
        super.onCreate()
        Log.v(LOG_TAG, "onCreate() super.onCreate() execution complete")

        if (mediaSession == null) {
            Log.d(LOG_TAG, "onCreate() mediaSession is null, calling MediaSessionCreator.create()")
            mediaSession = mediaSessionCreator.create(this)
        }


    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(LOG_TAG, "onBind() invoked with intent: ${intent?.action}")
        return super.onBind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.v(LOG_TAG, "onUnbind() invoked with intent: ${intent?.action}")
        return super.onUnbind(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.v(
            LOG_TAG,
            "onTaskRemoved() invoked with intent data: ${rootIntent?.data}, action: ${rootIntent?.action}"
        )
        savePlayerState()
        if (!(mediaSession?.player?.playWhenReady)!!) {
            Log.d(LOG_TAG, "onTaskRemoved() invoking stopSelf()")
            stopSelf()
            Log.d(LOG_TAG, "onTaskRemoved() call to stopSelf() complete")
        } else {
            Log.d(LOG_TAG, "onTaskRemoved() not invoking stopSelf()")
        }
    }

    override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
        Log.d(LOG_TAG, "onUpdateNotification() $session startInForeground $startInForegroundRequired")
        super.onUpdateNotification(session, startInForegroundRequired)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        Log.v(LOG_TAG, "onGetSession() invoked")
       return mediaSession
    }

    override fun stopService(name: Intent?): Boolean {
        Log.v(
            LOG_TAG,
            "stopService() invoked with intent data: ${name?.data}, action: ${name?.action}"
        )
        savePlayerState()
        return super.stopService(name)
    }


    override fun onDestroy() {
        Log.v(LOG_TAG, "onDestroy() invoked")
        savePlayerState()
        this.mediaSessionCreator.destroySession(this.mediaSession)
        clearListener()
        super.onDestroy()
        scope.cancel()
        Log.v(LOG_TAG, "onDestroy() invocation complete")
    }

    private fun savePlayerState() {
        Log.v(LOG_TAG, "savePlayerState() invoked")
        playerStateManager.saveState()
        Log.i(LOG_TAG, "savePlayerState() Player state saved")
    }


}