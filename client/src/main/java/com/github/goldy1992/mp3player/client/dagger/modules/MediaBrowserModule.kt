package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import com.github.goldy1992.mp3player.client.media.DefaultMediaBrowser2
import com.github.goldy1992.mp3player.commons.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ActivityRetainedComponent::class)
@Module
object MediaBrowserModule {

    @ActivityRetainedScoped
    @Provides
    fun providesDefaultMediaBrowser2Module(
        @ApplicationContext context: Context,
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
    ): DefaultMediaBrowser2 {
        return DefaultMediaBrowser2(context, mainDispatcher)
    }

}