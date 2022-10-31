package com.github.goldy1992.mp3player.client.dagger.modules

import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.MediaTestUtils
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [MediaControllerModule::class]
)
class MockMediaControllerModule {

    @ActivityRetainedScoped
    @Provides
    fun providesMockMediaController() : ListenableFuture<MediaController> {
        val mockMediaController = mock<MediaController>()
        whenever(mockMediaController.mediaMetadata).thenReturn(MediaTestUtils.createTestMediaMetaData())
        whenever(mockMediaController.isPlaying).thenReturn(false)
        return Futures.immediateFuture(mockMediaController)
    }
}