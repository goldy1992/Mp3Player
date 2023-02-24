package com.github.goldy1992.mp3player.service

import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.service.library.data.search.managers.SearchDatabaseManagers
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by Mike on 24/09/2017.
 *
 * Implementation of the MediaBrowserService. This service will run in the foreground when the front
 * end of the application interacts with it. When the application is closed or removed from the task
 * list, the service will continue to run in the background and only be destroyed by the operating
 * system.
 */
@AndroidEntryPoint
open class MediaPlaybackService : MediaLibraryService(),
        LogTagger {

    @Inject
    lateinit var mediaSessionCreator: MediaSessionCreator

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    private var mediaSession: MediaLibrarySession? = null

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var searchDatabaseManagers: SearchDatabaseManagers

    @Inject
    lateinit var playerStateManager: PlayerStateManager

    override fun onCreate() {
        Log.i(logTag(), "onCreate called")
        super.onCreate()
        Log.i(logTag(), "onCreate super called")

        if (mediaSession == null) {
            mediaSession = mediaSessionCreator.create(this)
        }

        scope.launch(ioDispatcher) {
            searchDatabaseManagers.reindexAll()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(logTag(), "onBind called with intent: ${intent?.action}")
        return super.onBind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(logTag(), "onUnbind called with intent: ${intent?.action}")
        return super.onUnbind(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.i(
            logTag(),
            "onTaskRemoved invoked with intent: ${rootIntent?.data}, action: ${rootIntent?.action}"
        )
        savePlayerState()
        if (!(mediaSession?.player?.playWhenReady)!!) {
            Log.i(logTag(), "stopping self")
            stopSelf()
        } else {
            Log.i(logTag(), "not stopping self")
        }
    }

    override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
      //  Log.i(logTag(), "onUpdateNotification, session: $session, startInForegroundRequired: $startInForegroundRequired")
        super.onUpdateNotification(session, startInForegroundRequired)
    }

    override fun onUpdateNotification(session: MediaSession) {
        //Log.i(logTag(), "onUpdateNotification, session: $session")
        super.onUpdateNotification(session)
    }

    @Suppress("DEPRECATION")
    private fun stopForegroundService(isPlaying : Boolean) {
        Log.i(logTag(), "called stopForegroundService")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!isPlaying) {
                stopForeground(STOP_FOREGROUND_REMOVE)
                Log.i(logTag(), "removed notification")
            } else {
                stopForeground(STOP_FOREGROUND_DETACH)
                Log.i(logTag(), "detached notification")
            }

        } else {
            if (!isPlaying) {
                stopForeground(true)
            } else {
                stopForeground(false)
            }

        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
       return mediaSession
    }

    override fun stopService(name: Intent?): Boolean {
        Log.i(logTag(), "Stopping service")
        savePlayerState()
        return super.stopService(name)
    }

    override fun onDestroy() {
        Log.i(logTag(), "onDestroy called")
        savePlayerState()
        this.mediaSessionCreator.destroySession(this.mediaSession)
        clearListener()
        super.onDestroy()
        Log.i(logTag(), "onDeStRoY complete")
    }

    private fun savePlayerState() {
        Log.i(logTag(), "saveState runBlocking")
        playerStateManager.saveState()
        Log.i(logTag(), "Player state saved")
    }

    override fun logTag() : String {
        return "MEDIA_PLAYBACK_SERVICE"
    }
}