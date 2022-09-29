package com.github.goldy1992.mp3player.client

import com.github.goldy1992.mp3player.client.callbacks.connection.MediaBrowserCompatCallback
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class MediaConnector

    @Inject
    constructor(private val mediaBrowserAdapter: MediaBrowserAdapter,
                private val clientMediaController: ClientMediaController,
                private val connectionCallback : MediaBrowserCompatCallback
    )
{
    fun connect() {
        connectionCallback.registerListener(clientMediaController)
        connectionCallback.registerListener(mediaBrowserAdapter)
        mediaBrowserAdapter.connect()
    }

    fun disconnect() {
        clientMediaController.disconnect()
        mediaBrowserAdapter.disconnect()
        connectionCallback.removeListener(mediaBrowserAdapter)
        connectionCallback.removeListener(clientMediaController)
    }
}