package com.github.goldy1992.mp3player.client

import android.content.Context
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.github.goldy1992.mp3player.commons.LogTagger

abstract class MediaAdapter (private var context : Context,
                             private var clientMediaController: ClientMediaController)
    :
    LogTagger, MediaControllerCompat.Callback() {

    override fun onConnected()  {
       clientMediaController.registerCallback(this)
    }

    open fun createMediaController(
        context: Context,
        token: MediaSessionCompat.Token
    ): MediaControllerCompat {
        return MediaControllerCompat(context, token)
    }
}