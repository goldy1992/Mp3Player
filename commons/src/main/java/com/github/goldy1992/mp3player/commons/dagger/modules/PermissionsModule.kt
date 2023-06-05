package com.github.goldy1992.mp3player.commons.dagger.modules

import com.github.goldy1992.mp3player.commons.data.repositories.permissions.DefaultPermissionsRepository
import com.github.goldy1992.mp3player.commons.data.repositories.permissions.IPermissionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PermissionsModule {

    @Singleton
    @Binds
    abstract fun providesIPermissionsRepository(defaultPermissionsRepository: DefaultPermissionsRepository) : IPermissionsRepository
}