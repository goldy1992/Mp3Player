package com.github.goldy1992.mp3player.client

import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MediaConnector
    @Inject
    constructor(val mediaBrowserAdapter: MediaBrowserAdapter,
                mediaControllerAdapter: MediaControllerAdapter,
    myConnectionCallback: MyConnectionCallback) {


}