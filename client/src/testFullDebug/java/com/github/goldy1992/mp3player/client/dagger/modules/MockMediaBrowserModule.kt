package com.github.goldy1992.mp3player.client.dagger.modules

import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.MediaTestUtils.createTestMediaItem
import com.github.goldy1992.mp3player.client.media.MediaBrowserTestImpl
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [MediaBrowserModule::class]
)
class MockMediaBrowserModule {

    @ActivityRetainedScoped
    @Provides
    fun providesMockMediaBrowser() : MediaBrowserTestImpl {
        return MediaBrowserTestImpl()
    }
}