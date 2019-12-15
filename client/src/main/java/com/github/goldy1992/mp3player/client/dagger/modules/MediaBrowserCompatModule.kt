package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.Module
import dagger.Provides

@Module
class MediaBrowserCompatModule {
    @Provides
    fun provideMediaBrowserCompat(context: Context, componentClassMapper: ComponentClassMapper,
                                  myConnectionCallback: MyConnectionCallback?): MediaBrowserCompat {
        val componentName = ComponentName(context, componentClassMapper.service!!)
        return MediaBrowserCompat(context, componentName, myConnectionCallback, null)
    }
}