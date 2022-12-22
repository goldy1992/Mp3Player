package com.github.goldy1992.mp3player.client.data.sources

import androidx.media3.session.MediaBrowser
import org.junit.Assert.*
import org.junit.Before
import org.mockito.kotlin.stub

class DefaultPlaybackDataSourceTest {
    @Before
    fun setup() {
        MediaBrowser.Builder().setListener(MediaBrowser.Listener).stub {  }
    }
}