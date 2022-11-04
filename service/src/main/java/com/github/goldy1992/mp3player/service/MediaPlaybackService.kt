package com.github.goldy1992.mp3player.service

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.common.Player.STATE_READY
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.service.library.CustomMediaItemTree
import com.github.goldy1992.mp3player.service.library.content.observers.MediaStoreObservers
import com.github.goldy1992.mp3player.service.library.search.managers.SearchDatabaseManagers
import com.google.common.collect.ImmutableList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        PlayerNotificationManager.NotificationListener {

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

    private var customLayout = ImmutableList.of<CommandButton>()

    private lateinit var mediaSession: MediaLibrarySession

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var player : ExoPlayer

    @Inject
    lateinit var mediaStoreObservers : MediaStoreObservers

    @Inject
    lateinit var searchDatabaseManagers: SearchDatabaseManagers

    @Inject
    lateinit var customMediaItemTree: CustomMediaItemTree

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(logTag(), "on start command")
        scope.launch(ioDispatcher) {
            searchDatabaseManagers.reindexAll()
        }



        if (!customLayout.isEmpty()) {
            // Send custom layout to legacy session.
            mediaSession.setCustomLayout(customLayout)
        }
        mediaStoreObservers.init(mediaSession)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        Log.i(logTag(), "onCreate called")
        super.onCreate()
        mediaSession = mediaSessionCreator.create(this, componentClassMapper, player, mediaLibrarySessionCallback)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        val playbackState : Int = mediaSession.player.playbackState
        Log.i(logTag(), "TASK rEmOvEd, playback state: " + Constants.playbackStateDebugMap.get(playbackState))

        if (playbackState != STATE_READY) {
            stopForeground(true)
        } else {
            stopForeground(false)
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
       return mediaSession
    }

    override fun onUpdateNotification(session: MediaSession) {
        super.onUpdateNotification(session)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mediaSession.release()
        Log.i(logTag(), "onDeStRoY")
        mediaStoreObservers.unregisterAll()
    }


    override fun logTag() : String {
        return "MEDIA_PLAYBACK_SERVICE"
    }
}