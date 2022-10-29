package com.github.goldy1992.mp3player.client

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.github.goldy1992.mp3player.client.ui.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mockito.kotlin.mock


class MockUserPreferencesRepository : UserPreferencesRepository(mock<DataStore<Preferences>>()) {

        override fun getTheme(): Flow<Theme> {
            return flow {
                emit(Theme.BLUE)
            }
        }

        override fun getDarkMode(): Flow<Boolean> {
                return flow {
                        emit(true)
                }
        }

        override fun getSystemDarkMode(): Flow<Boolean> {
                return flow {
                        emit(true)
                }
        }
}