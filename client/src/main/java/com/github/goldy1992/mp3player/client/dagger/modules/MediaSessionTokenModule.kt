package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.SessionToken
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
class MediaSessionTokenModule {

    @ActivityRetainedScoped
    @Provides
    fun providesSessionToken(@ApplicationContext context: Context,
                                  componentClassMapper: ComponentClassMapper):
            SessionToken {
        val componentName = ComponentName(context, componentClassMapper.service!!)
        return SessionToken(context, componentName)
    }
}
