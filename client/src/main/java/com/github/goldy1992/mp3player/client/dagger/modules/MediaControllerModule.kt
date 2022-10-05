package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
class MediaControllerModule {

    @ActivityRetainedScoped
    @Provides
    fun providesMediaControllerFuture(@ApplicationContext context: Context,
                                        sessionToken: SessionToken)
            : ListenableFuture<MediaController> {
        return MediaController.Builder(context, sessionToken).buildAsync()
    }
}