package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import com.github.goldy1992.mp3player.client.MockMediaControllerAdapter
import dagger.Module
import dagger.Provides

@Module
class MockMediaControllerAdapterModule {
    @Provides
    fun provideMediaControllerAdapter(context: Context?, mediaBrowserCompat: MediaBrowserCompat, myMediaControllerCallback: MyMediaControllerCallback): MediaControllerAdapter {
        return MockMediaControllerAdapter(context, mediaBrowserCompat, myMediaControllerCallback)
    }
}