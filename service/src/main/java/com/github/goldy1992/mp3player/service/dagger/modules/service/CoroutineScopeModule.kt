package com.github.goldy1992.mp3player.service.dagger.modules.service

import com.github.goldy1992.mp3player.commons.DefaultDispatcher
import com.github.goldy1992.mp3player.commons.ServiceCoroutineScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@InstallIn(ServiceComponent::class)
@Module
object CoroutineScopeModule {
    @ServiceCoroutineScope
    @ServiceScoped
    @Provides
    fun providesCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)
}