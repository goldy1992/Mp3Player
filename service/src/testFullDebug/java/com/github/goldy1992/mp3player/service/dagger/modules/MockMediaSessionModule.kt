package com.github.goldy1992.mp3player.service.dagger.modules

import com.github.goldy1992.mp3player.service.MediaSessionCreator
import com.github.goldy1992.mp3player.service.MockMediaSessionCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(components = [ServiceComponent::class],
    replaces = [MediaSessionCreatorModule::class])
@Module
class MockMediaSessionModule {

    @ServiceScoped
    @Provides
    fun providesMockMediaSession() : MediaSessionCreator {
        return MockMediaSessionCreator()
    }

}