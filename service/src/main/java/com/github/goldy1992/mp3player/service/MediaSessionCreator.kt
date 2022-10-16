package com.github.goldy1992.mp3player.service

import androidx.media3.common.Player
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.MediaLibrarySession

open class MediaSessionCreator {
    open fun create(service : MediaLibraryService, player: Player, callback: MediaLibrarySessionCallback) : MediaLibrarySession {
        return MediaLibrarySession.Builder(service, player, callback)
            .build()
    }
}