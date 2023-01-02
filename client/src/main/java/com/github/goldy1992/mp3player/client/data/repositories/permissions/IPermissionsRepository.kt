package com.github.goldy1992.mp3player.client.data.repositories.permissions

import kotlinx.coroutines.flow.Flow

interface IPermissionsRepository {

    fun hasExternalStorageAccessFlow() : Flow<Boolean>
}