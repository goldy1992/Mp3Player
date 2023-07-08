package com.github.goldy1992.mp3player.commons.dagger.modules

import com.github.goldy1992.mp3player.commons.DefaultDispatcher
import com.github.goldy1992.mp3player.commons.SingletonCoroutineScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton



@InstallIn(SingletonComponent::class)
@Module
object SingletonScopedCoroutineScopeModule {

    @SingletonCoroutineScope
    @Singleton
    @Provides
    fun providesCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)
}