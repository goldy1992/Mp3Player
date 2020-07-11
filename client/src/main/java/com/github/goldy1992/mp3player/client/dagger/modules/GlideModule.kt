package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class GlideModule {

    @ActivityScoped
    @Provides
    fun providesRequestManager(@ActivityContext context: Context?): RequestManager {
        return Glide.with(context!!)
    }
}