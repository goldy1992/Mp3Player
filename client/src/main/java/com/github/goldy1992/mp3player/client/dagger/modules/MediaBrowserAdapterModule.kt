package com.github.goldy1992.mp3player.client.dagger.modules

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Module
import dagger.Provides

@Module
class MediaBrowserAdapterModule {

    @ComponentScope
    @Provides
    fun provideMediaBrowserAdapter(mediaBrowser: MediaBrowserCompat,
                                   mySubscriptionCallback: MediaIdSubscriptionCallback,
                                   mySearchCallback: MySearchCallback) : MediaBrowserAdapter {
        return MediaBrowserAdapter(mediaBrowser, mySubscriptionCallback, mySearchCallback)
    }
}