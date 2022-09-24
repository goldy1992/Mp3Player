package com.github.goldy1992.mp3player.client.dagger.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@InstallIn(ActivityRetainedComponent::class)
@Module
class ActivityRetainedCoroutineScopeModule {

    @ActivityRetainedScoped
    @Provides
    fun providesCoroutineScope() : CoroutineScope {
        return CoroutineScope(Dispatchers.Main + SupervisorJob())
    }
}