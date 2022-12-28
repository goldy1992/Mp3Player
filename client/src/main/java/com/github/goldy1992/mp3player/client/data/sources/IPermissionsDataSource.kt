package com.github.goldy1992.mp3player.client.data.sources

import kotlinx.coroutines.flow.Flow

interface IPermissionsDataSource {
    fun hasExternalStorageAccessFlow() : Flow<Boolean>
}