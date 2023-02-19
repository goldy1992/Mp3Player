package com.github.goldy1992.mp3player.service

import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.core.net.toUri
import androidx.media3.common.Player
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.Constants.ROOT_APP_URI_PATH
import com.github.goldy1992.mp3player.commons.Screen
import com.github.goldy1992.mp3player.service.player.equalizer.FFTAudioProcessor
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
open class

    MediaSessionCreator
    @Inject
    constructor(
        private val fftAudioProcessor: FFTAudioProcessor
    ) {
    open fun create(service : MediaLibraryService,
                    componentClassMapper: ComponentClassMapper,
                    player: Player,
                    callback: MediaLibrarySessionCallback) : MediaLibrarySession {

        val intent = Intent(
            Intent.ACTION_VIEW,
            null,
            service,
            componentClassMapper.mainActivity)

        val task = TaskStackBuilder
            .create(service.applicationContext)
            .addNextIntentWithParentStack(intent)
            .run {
                val immutableFlag = FLAG_IMMUTABLE
                getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)
            }
        val session = MediaLibrarySession.Builder(service, player, callback)
            .setSessionActivity(task)
            .build()
        fftAudioProcessor.mediaSession = session
        return session
    }
}