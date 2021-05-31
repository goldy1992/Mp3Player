package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
class MediaBrowserCompatModule {

    @ActivityRetainedScoped
    @Provides
    fun provideMediaBrowserCompat(@ApplicationContext context: Context,
                                  componentClassMapper: ComponentClassMapper,
                                  myConnectionCallback: MyConnectionCallback):
            MediaBrowserCompat {
        val componentName = ComponentName(context, componentClassMapper.service!!)
        return MediaBrowserCompat(context, componentName, myConnectionCallback, null)
    }
}