package com.github.goldy1992.mp3player.client.data.repositories.permissions

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPermissionsRepository

@Inject
constructor() : IPermissionsRepository {
    override fun hasExternalStorageAccessFlow(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}