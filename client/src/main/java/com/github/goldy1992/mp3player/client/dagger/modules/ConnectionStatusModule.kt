package com.github.goldy1992.mp3player.client.dagger.modules

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.callbacks.connection.ConnectionStatus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@InstallIn(ActivityRetainedComponent::class)
@Module
class ConnectionStatusModule {

    @ActivityRetainedScoped
    @Provides
    fun providesConnectionStatus() : MutableLiveData<ConnectionStatus> {
        return MutableLiveData(ConnectionStatus.NOT_CONNECTED)
    }
}