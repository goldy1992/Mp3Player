package com.github.goldy1992.mp3player.client

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

class MockDataStore(override val data: Flow<Preferences>) : DataStore<Preferences> {

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        TODO("Not yet implemented")
    }
}