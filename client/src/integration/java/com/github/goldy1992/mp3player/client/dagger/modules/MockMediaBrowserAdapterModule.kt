package com.github.goldy1992.mp3player.client.dagger.integration.modules

import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Module
import dagger.Provides

@Module
class MockMediaBrowserAdapterModule {

    @ComponentScope
    @Provides
    fun provideMockMediaBrowserAdapter(mediaIdSubscriptionCallback: MediaIdSubscriptionCallback,
                                       mySearchCallback: MySearchCallback) : MediaBrowserAdapter {
        return MockMediaBrowserAdapter(mediaIdSubscriptionCallback, mySearchCallback)
    }
}