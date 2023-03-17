package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.github.goldy1992.mp3player.client.AsyncMediaBrowserListener
import com.github.goldy1992.mp3player.client.media.DefaultMediaBrowser
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@InstallIn(ActivityRetainedComponent::class)
@Module
object MediaBrowserModule {

    @ActivityRetainedScoped
    @Provides
    fun providesMediaBrowserFuture(@ApplicationContext context: Context,
                                      sessionToken: SessionToken,
                                   asyncMediaBrowserListener : AsyncMediaBrowserListener
    )
            : ListenableFuture<MediaBrowser> {
        return MediaBrowser
            .Builder(context, sessionToken)
            .setListener(asyncMediaBrowserListener)
            .buildAsync()
    }

    @ActivityRetainedScoped
    @Provides
    fun providesDefaultMediaBrowserModule(@ApplicationContext context: Context,
                                          sessionToken: SessionToken,
                                          scope : CoroutineScope,
                                          @MainDispatcher mainDispatcher : CoroutineDispatcher
    ) : DefaultMediaBrowser  {
        return DefaultMediaBrowser(context, sessionToken, scope, mainDispatcher)
    }


}