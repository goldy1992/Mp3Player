package com.github.goldy1992.mp3player.service.dagger.modules.service

import com.github.goldy1992.mp3player.service.library.content.retrievers.ContentRetrievers
import com.github.goldy1992.mp3player.service.library.content.retrievers.ContentRetrieversDefaultImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
abstract class ContentRetrieversBindsModule {

    @Binds
    @ServiceScoped
    abstract fun bindsContentRetrievers(contentRetrieversDefaultImpl: ContentRetrieversDefaultImpl) : ContentRetrievers
}