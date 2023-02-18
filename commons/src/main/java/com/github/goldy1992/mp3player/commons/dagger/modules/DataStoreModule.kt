package com.github.goldy1992.mp3player.commons.dagger.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.github.goldy1992.mp3player.commons.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    private val Context.dataStore by preferencesDataStore(
        name = Constants.USER_PREFERENCES_NAME
    )
    @Singleton
    @Provides
    fun providesDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return context.dataStore
    }
}