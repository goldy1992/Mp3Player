package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import org.mockito.kotlin.mock

abstract class MediaBrowserListenerFlowTestBase<T> : MediaFlowTestBase<T>() {

    protected val listeners = mutableSetOf<MediaBrowser.Listener>()
    protected val controller = mock<MediaBrowser>()
    protected val addListener = { listener : MediaBrowser.Listener ->
        listeners.add(listener)
        true
    }
    protected val removeListener = { listener : MediaBrowser.Listener ->
        listeners.remove(listener)
        true
    }

    protected fun invoke(lbda : (MediaBrowser.Listener) -> Unit ) {
        for (listener in listeners) {
            lbda(listener)
        }
    }
}