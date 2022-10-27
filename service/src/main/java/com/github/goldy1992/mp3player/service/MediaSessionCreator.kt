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
import com.github.goldy1992.mp3player.commons.Screen

open class MediaSessionCreator {
    open fun create(service : MediaLibraryService,
                    componentClassMapper: ComponentClassMapper,
                    player: Player,
                    callback: MediaLibrarySessionCallback) : MediaLibrarySession {

        val intent = Intent(
            Intent.ACTION_VIEW,
            "com.github.goldy1992.mp3player/${Screen.NOW_PLAYING.name}".toUri(),
            service,
            componentClassMapper.mainActivity)

        val task = TaskStackBuilder
            .create(service.applicationContext)
            .addNextIntentWithParentStack(intent)
            .run {
                val immutableFlag = FLAG_IMMUTABLE
                getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)
            }
        return MediaLibrarySession.Builder(service, player, callback)
            .setSessionActivity(task)
            .build()
    }
}