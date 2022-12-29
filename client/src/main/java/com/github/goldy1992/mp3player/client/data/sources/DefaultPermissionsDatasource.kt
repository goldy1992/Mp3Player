package com.github.goldy1992.mp3player.client.data.sources

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPermissionsDatasource

    @Inject
    constructor() : IPermissionsDataSource {
    override fun hasExternalStorageAccessFlow(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}