package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import com.github.goldy1992.mp3player.client.PermissionsProcessor
import com.github.goldy1992.mp3player.client.activities.MainActivity
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
    fun providesPermissionProcessor(@ActivityContext context: Context) : PermissionsProcessor {
        return PermissionsProcessor(context = context,permissionGranted = context as MainActivity)
    }
}