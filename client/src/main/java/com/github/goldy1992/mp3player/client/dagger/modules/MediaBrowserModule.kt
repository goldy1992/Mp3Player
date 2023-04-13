package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import androidx.media3.session.SessionToken
import com.github.goldy1992.mp3player.client.media.DefaultMediaBrowser
import com.github.goldy1992.mp3player.commons.MainDispatcher
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
    fun providesDefaultMediaBrowserModule(
        @ApplicationContext context: Context,
        sessionToken: SessionToken,
        scope: CoroutineScope,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): DefaultMediaBrowser {
        return DefaultMediaBrowser(context, sessionToken, scope, mainDispatcher)
    }

}