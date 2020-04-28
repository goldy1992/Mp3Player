package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Module
import dagger.Provides

@Module
class GlideModule {

    @ComponentScope
    @Provides
    fun providesRequestManager(context: Context?): RequestManager {
        return Glide.with(context!!)
    }
}