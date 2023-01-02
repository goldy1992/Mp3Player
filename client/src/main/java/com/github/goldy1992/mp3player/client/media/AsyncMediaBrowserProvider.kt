package com.github.goldy1992.mp3player.client.media

import android.content.Context
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AsyncMediaBrowserProvider

    @Inject
    constructor(@ApplicationContext private val context: Context,
                private val sessionToken: SessionToken
    ) {

    fun getAsyncMediaBrowser(listener : MediaBrowser.Listener)
        : ListenableFuture<MediaBrowser> {
        return MediaBrowser
            .Builder(context, sessionToken)
            .setListener(listener)
            .buildAsync()
    }
}