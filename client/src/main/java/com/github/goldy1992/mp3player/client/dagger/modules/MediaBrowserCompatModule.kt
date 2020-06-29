package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class MediaBrowserCompatModule {

    @ActivityScoped
    @Provides
    fun provideMediaBrowserCompat(@ApplicationContext context: Context,
                                  componentClassMapper: ComponentClassMapper,
                                  myConnectionCallback: MyConnectionCallback):
            MediaBrowserCompat {
        val componentName = ComponentName(context, componentClassMapper.service!!)
        return MediaBrowserCompat(context, componentName, myConnectionCallback, null)
    }
}