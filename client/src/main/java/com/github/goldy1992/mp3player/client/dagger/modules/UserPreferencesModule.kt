package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.USER_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UserPreferencesModule {

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    @Singleton
    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context) : UserPreferencesRepository {
        return UserPreferencesRepository(dataStore = context.dataStore)
    }
}