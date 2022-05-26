package com.github.goldy1992.mp3player.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.content.observers.MediaStoreObservers
import com.github.goldy1992.mp3player.service.library.search.managers.SearchDatabaseManagers
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
open class MediaPlaybackService : MediaBrowserServiceCompat(),
        CoroutineScope by GlobalScope,
        LogTagger,
        PlayerNotificationManager.NotificationListener {

    private lateinit var contentManager: ContentManager

    private lateinit var mediaSessionConnectorCreator: MediaSessionConnectorCreator

    private lateinit var mediaSession: MediaSessionCompat

    private var rootAuthenticator: RootAuthenticator? = null
    private var mediaStoreObservers: MediaStoreObservers? = null
    private var searchDatabaseManagers: SearchDatabaseManagers? = null

    override fun onCreate() {
        Log.i(logTag(), "onCreate called")
        super.onCreate()
        mediaSessionConnectorCreator.create()
        this.sessionToken = mediaSession.sessionToken
        mediaStoreObservers!!.init(this)
        launch(Dispatchers.IO) {
            searchDatabaseManagers!!.reindexAll()
        }
    }

    override fun onStartCommand(intent: Intent?,
                                flags: Int,
                                startId: Int): Int {
        Log.i(logTag(), "breakpoint, on start command called")
        return Service.START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        val playbackState : Int? = mediaSession.controller.playbackState?.state
        Log.i(logTag(), "TASK rEmOvEd, playback state: " + Constants.playbackStateDebugMap.get(playbackState ?: PlaybackStateCompat.STATE_NONE))

        if (playbackState == null || playbackState != PlaybackStateCompat.STATE_PLAYING) {
            stopForeground(true)
        } else {
            stopForeground(false)
        }
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int,
                           rootHints: Bundle?): BrowserRoot? {
        return rootAuthenticator!!.authenticate(clientPackageName, clientUid, rootHints)
    }

    /**
     * onLoadChildren(String, Result, Bundle) :- onLoadChildren should always be called with a LibraryObject item as a bundle option. Searching for
     * a MediaItem's children is now deprecated as it wasted
     * @param parentId the parent ID
     * @param result the result object used by the MediaBrowserServiceCompat
     */
    override fun onLoadChildren(parentId: String, result: Result<List<MediaBrowserCompat.MediaItem>>) { //  Browsing not allowed
        if (rootAuthenticator!!.rejectRootSubscription(parentId)) {
            result.sendResult(null)
            return
        }
        result.detach()
        runBlocking {
            launch(Dispatchers.Default) {
                // Assume for example that the music catalog is already loaded/cached.
                val mediaItems = contentManager.getChildren(parentId)
                result.sendResult(mediaItems)
                println("finish coroutine")
            }.join()
            println("finished on load children")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSearch(query: String, extras: Bundle?,
                          result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.detach()
        runBlocking {
            launch(Dispatchers.Default) {
                // Assume for example that the music catalog is already loaded/cached.
                val mediaItems = contentManager.search(query)
                result.sendResult(mediaItems as MutableList<MediaBrowserCompat.MediaItem>)
            }.join()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mediaSession.release()
        Log.i(logTag(), "onDeStRoY")
        if (this.coroutineContext.isActive) {
            this.cancel()
        }
        mediaStoreObservers!!.unregisterAll()
    }


    @Inject
    fun setRootAuthenticator(rootAuthenticator: RootAuthenticator?) {
        this.rootAuthenticator = rootAuthenticator
    }

    @Inject
    fun setMediaSessionConnectorCreator(mediaSessionConnectorCreator: MediaSessionConnectorCreator) {
        this.mediaSessionConnectorCreator = mediaSessionConnectorCreator
    }

    @Inject
    fun setMediaSession(mediaSession : MediaSessionCompat) {
        this.mediaSession = mediaSession
    }

    @Inject
    fun setMediaStoreObservers(mediaStoreObservers: MediaStoreObservers?) {
        this.mediaStoreObservers = mediaStoreObservers
    }

    @Inject
    fun setSearchDatabaseManagers(searchDatabaseManagers: SearchDatabaseManagers?) {
        this.searchDatabaseManagers = searchDatabaseManagers
    }

    @Inject
    fun setContentManager(contentManager: ContentManager) {
        this.contentManager = contentManager
    }

    override fun logTag() : String {
        return "MEDIA_PLAYBACK_SERVICE"
    }
}