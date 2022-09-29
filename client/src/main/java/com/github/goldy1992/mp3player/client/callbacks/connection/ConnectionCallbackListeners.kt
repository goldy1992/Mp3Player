package com.github.goldy1992.mp3player.client.callbacks.connection

import com.github.goldy1992.mp3player.client.AudioDataAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class ConnectionCallbackListeners

@Inject
constructor(
     mediaBrowserAdapter: MediaBrowserAdapter,
     mediaControllerAdapter: MediaControllerAdapter,
     audioDataAdapter: AudioDataAdapter) {

    val listeners = setOf<MediaBrowserConnectionListener>(mediaBrowserAdapter, mediaControllerAdapter, audioDataAdapter)

}