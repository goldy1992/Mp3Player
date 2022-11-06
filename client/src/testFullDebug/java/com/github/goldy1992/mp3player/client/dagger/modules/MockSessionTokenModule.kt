package com.github.goldy1992.mp3player.client.dagger.modules

import androidx.media3.session.SessionToken
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn
import org.mockito.kotlin.mock

@Module
@TestInstallIn(components = [ActivityRetainedComponent::class],
replaces = [MediaSessionTokenModule::class])
class MockSessionTokenModule {

    @ActivityRetainedScoped
    @Provides
    fun providesMockSessionToken() : SessionToken {
        return mock()
    }
}