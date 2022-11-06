package com.github.goldy1992.mp3player.service

import androidx.media3.common.Player
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MockMediaSessionCreator : MediaSessionCreator() {

    override fun create(
        service: MediaLibraryService,
        componentClassMapper: ComponentClassMapper,
        player: Player,
        callback: MediaLibrarySessionCallback
    ): MediaLibrarySession {
        val mockMediaLibrarySession = mock<MediaLibrarySession>()
        whenever(mockMediaLibrarySession.player).thenReturn(player)
        return mockMediaLibrarySession
    }
}