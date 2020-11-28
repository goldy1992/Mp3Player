package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MockMediaBrowserAdapter
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class MockMediaBrowserAdapterModule {

    @ActivityScoped
    @Provides
    fun provideMockMediaBrowserAdapter(mediaIdSubscriptionCallback: MediaIdSubscriptionCallback,
                                       mySearchCallback: MySearchCallback) : MediaBrowserAdapter {
        return MockMediaBrowserAdapter(mediaIdSubscriptionCallback, mySearchCallback)
    }
}