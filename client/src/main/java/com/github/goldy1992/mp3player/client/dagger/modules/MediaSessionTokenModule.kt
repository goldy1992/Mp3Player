package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.ComponentName
import android.content.Context
import androidx.concurrent.futures.await
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@InstallIn(ActivityRetainedComponent::class)
@Module
class MediaSessionTokenModule {

    @ActivityRetainedScoped
    @Provides
    fun providesSessionToken(@ApplicationContext context: Context,
                                  componentClassMapper: ComponentClassMapper):
            SessionToken {
        val componentName = ComponentName(context, componentClassMapper.service!!)
        return SessionToken(context, componentName)
    }

    @ActivityRetainedScoped
    @Provides
    suspend fun providesMediaBrowser(@ApplicationContext context: Context, sessionToken: SessionToken,
    scope : CoroutineScope) : MediaBrowser {
        return MediaBrowser.Builder(context, sessionToken).buildAsync().await()
    }

    @ActivityRetainedScoped
    @Provides
    fun providesMediaController(@ApplicationContext context: Context, sessionToken: SessionToken) : ListenableFuture<MediaController> {
        return MediaController.Builder(context, sessionToken).buildAsync()
    }
}
