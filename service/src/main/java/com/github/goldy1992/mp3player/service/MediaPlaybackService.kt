package com.github.goldy1992.mp3player.service

import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_READY
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.content.observers.MediaStoreObservers
import com.github.goldy1992.mp3player.service.library.search.managers.SearchDatabaseManagers
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
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
        PlayerNotificationManager.NotificationListener {

    @Inject
    lateinit var mediaLibrarySessionCallback : MediaLibrarySessionCallback

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

    override fun onCreate() {
        Log.i(logTag(), "onCreate called")
        super.onCreate()

        scope.launch(Dispatchers.IO) {
            searchDatabaseManagers.reindexAll()
        }

        val sessionActivityPendingIntent =
            TaskStackBuilder.create(this).run {
//                addNextIntent(Intent(this@PlaybackService, MainActivity::class.java))
//                addNextIntent(Intent(this@PlaybackService, PlayerActivity::class.java))

                val immutableFlag = if (Build.VERSION.SDK_INT >= 23) FLAG_IMMUTABLE else 0
                getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)
            }

        mediaSession =
            MediaLibrarySession.Builder(this, player, mediaLibrarySessionCallback)
                .setSessionActivity(sessionActivityPendingIntent)
                .build()

        if (!customLayout.isEmpty()) {
            // Send custom layout to legacy session.
            mediaSession.setCustomLayout(customLayout)
        }
        mediaStoreObservers.init(mediaSession)
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

    override fun onDestroy() {
        super.onDestroy()
        this.mediaSession.release()
        Log.i(logTag(), "onDeStRoY")
        mediaStoreObservers!!.unregisterAll()
    }


    override fun logTag() : String {
        return "MEDIA_PLAYBACK_SERVICE"
    }
}