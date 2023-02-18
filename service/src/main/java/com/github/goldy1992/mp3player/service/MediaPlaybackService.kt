package com.github.goldy1992.mp3player.service

import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.Player.State
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.commons.Constants.playbackStateDebugMap
import com.github.goldy1992.mp3player.commons.PermissionsUtils.appHasPermissions
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.content.observers.MediaStoreObservers
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
        LogTagger,
        PlayerNotificationManager.NotificationListener,
        PermissionsListener{

    @Inject
    lateinit var mediaSessionCreator: MediaSessionCreator

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    @MainDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher

    @Inject
    lateinit var componentClassMapper : ComponentClassMapper

    @Inject
    lateinit var mediaLibrarySessionCallback : MediaLibrarySessionCallback

    @Inject
    lateinit var rootAuthenticator: RootAuthenticator

    private var customLayout = listOf<CommandButton>()

    private var mediaSession: MediaLibrarySession? = null

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var player : ExoPlayer

    @Inject
    lateinit var mediaStoreObservers : MediaStoreObservers

    @Inject
    lateinit var searchDatabaseManagers: SearchDatabaseManagers

    @Inject
    lateinit var contentManager: ContentManager

    @Inject
    lateinit var playerStateManager: PlayerStateManager

    @Inject
    lateinit var permissionsNotifier: PermissionsNotifier

    var isInitialised = false

    private fun shouldInitialise() : Boolean {
        return !isInitialised && appHasPermissions(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.i(logTag(), "on start command: intent: ${intent?.action}, startId: $startId")
        if (shouldInitialise()) {
            Log.i(logTag(), "initialising media library")
            initialise()

        } else {
            Log.i(logTag(), "media library already initialised")
        }
        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    private fun initialise() {
        Log.i(logTag(), "Initialising MediaPlaybackService with player: ${player}")
        if (mediaSession == null) {
            mediaSession = mediaSessionCreator.create(
                this,
                componentClassMapper,
                player,
                mediaLibrarySessionCallback
            )
        }

        val rootItem = rootAuthenticator.getRootItem()
        runBlocking {
            contentManager.initialise(rootMediaItem = rootItem)
            playerStateManager.loadPlayerState()
        }

        scope.launch(ioDispatcher) {
            searchDatabaseManagers.reindexAll()
        }


        if (!customLayout.isEmpty()) {
            // Send custom layout to legacy session.
            mediaSession!!.setCustomLayout(customLayout)
        }
        mediaStoreObservers.init(mediaSession!!)
        this.isInitialised = true
    }

    override fun onCreate() {
        Log.i(logTag(), "onCreate called")
        super.onCreate()
        Log.i(logTag(), "onCreate super called")
        permissionsNotifier.addListener(this)

        if (shouldInitialise()) {
            initialise()
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

        Log.i(logTag(), "onTaskRemoved invoked")
        val isPlaying : Boolean? = mediaSession?.player?.isPlaying
        Log.i(logTag(), "TASK rEmOvEd, isPlaying: $isPlaying")

        stopForegroundService(isPlaying ?: false)
        super.onTaskRemoved(rootIntent)
    }

    @Suppress("DEPRECATION")
    private fun stopForegroundService(isPlaying : Boolean) {
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

        savePlayerState()

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

        this.mediaSession?.run {
            player.release()
            release()
            mediaSession = null

        }

        mediaStoreObservers.unregisterAll()
        super.onDestroy()
        Log.i(logTag(), "onDeStRoY complete")
    }

    override fun onPermissionsGranted() {
        Log.i(logTag(), "permissions were granted")
        if (shouldInitialise()) {
            initialise()
        }
    }

    private fun savePlayerState() {
        scope.launch {
            Log.i(logTag(), "saveState runBlocking")
            playerStateManager.saveState()
            Log.i(logTag(), "Player state saved")
        }
    }

    override fun logTag() : String {
        return "MEDIA_PLAYBACK_SERVICE"
    }


}