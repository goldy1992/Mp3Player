package com.github.goldy1992.mp3player.service

import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.media3.common.Player
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.content.observers.MediaStoreObservers
import com.github.goldy1992.mp3player.service.player.equalizer.FFTAudioProcessor
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
open class

    MediaSessionCreator
    @Inject
    constructor(
        private val fftAudioProcessor: FFTAudioProcessor,
        private val contentManager: ContentManager,
        private val componentClassMapper: ComponentClassMapper,
        private val player : Player,
        private val mediaLibrarySessionCallback : MediaLibrarySessionCallback,
        private val mediaStoreObservers : MediaStoreObservers
    ) : LogTagger {

    private var customLayout = listOf<CommandButton>()

    open fun create(service : MediaLibraryService) : MediaLibrarySession {

        val intent = Intent(
            Intent.ACTION_VIEW,
            null,
            service,
            componentClassMapper.mainActivity)


        val task = TaskStackBuilder
            .create(service.applicationContext)
            .addNextIntent(intent)
            .run {

                val immutableFlag = if (Build.VERSION.SDK_INT >= 23) FLAG_IMMUTABLE else 0
                getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)

            }
        val session = MediaLibrarySession.Builder(service, player, mediaLibrarySessionCallback)
            .setSessionActivity(task)
            .build()

        if (!customLayout.isEmpty()) {
            // Send custom layout to legacy session.
            session.setCustomLayout(customLayout)
        }

        fftAudioProcessor.mediaSession = session
        contentManager.mediaSession = session
        mediaStoreObservers.init(session)
        return session
    }

    fun destroySession(session: MediaLibrarySession?) {
        session?.run {
            Log.i(logTag(), "Releasing Exoplayer")
            player.release()
            Log.i(logTag(), "Exoplayer released, releasing media session")
            release()
            Log.i(logTag(), "media session released")
        }
        mediaStoreObservers.unregisterAll()
    }

    override fun logTag(): String {
        return "MediaSessionCreator"
    }
}