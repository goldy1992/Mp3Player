package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class MediaControllerAdapterModule {

    @ActivityScoped
    @Provides
    fun providesMediaControllerAdapter(@ApplicationContext context: Context,
                                       mediaBrowserCompat: MediaBrowserCompat,
                                       myMediaControllerCallback: MyMediaControllerCallback)
            : MediaControllerAdapter {
        return MediaControllerAdapter(context, mediaBrowserCompat, myMediaControllerCallback)
    }
}