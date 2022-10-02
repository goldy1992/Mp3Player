package com.github.goldy1992.mp3player.service.dagger.modules.service

import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.service.MyForwardingPlayer

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@InstallIn(ServiceComponent::class)
@Module
abstract class ExoPlayerBindModule {

    @Binds
    abstract fun bindsForwardingPlayer(myForwardingPlayer: MyForwardingPlayer) : ForwardingPlayer

    @Binds
    abstract fun bindsPlayer(player: ForwardingPlayer) : Player
}