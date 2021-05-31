package com.github.goldy1992.mp3player.client.dagger.modules

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.callbacks.connection.ConnectionStatus
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
class MediaBrowserAdapterModule {

    @ActivityRetainedScoped
    @Provides
    fun provideMediaBrowserAdapter(mediaBrowser: MediaBrowserCompat,
                                   mySubscriptionCallback: MediaIdSubscriptionCallback,
                                   mySearchCallback: MySearchCallback) : MediaBrowserAdapter {
        return MediaBrowserAdapter(mediaBrowser, mySubscriptionCallback, mySearchCallback)
    }
}