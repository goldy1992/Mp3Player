package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.callbacks.connection.ConnectionStatus
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@InstallIn(ActivityRetainedComponent::class)
@Module
class MediaBrowserCompatModule {

    @ActivityRetainedScoped
    @Provides
    fun providesConnectionStatus() : MutableLiveData<ConnectionStatus> {
        return MutableLiveData(ConnectionStatus.NOT_CONNECTED)
    }


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