package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import com.github.goldy1992.mp3player.client.activities.MainActivity
import com.github.goldy1992.mp3player.client.permissions.CompatWrapper
import com.github.goldy1992.mp3player.client.permissions.PermissionsProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class PermissionsModule {

    @ActivityScoped
    @Provides
    fun providesPermissionProcessor(@ActivityContext context: Context, compatWrapper: CompatWrapper) : PermissionsProcessor {
        return PermissionsProcessor(permissionGranted = context as MainActivity, compatWrapper =  compatWrapper)
    }
}