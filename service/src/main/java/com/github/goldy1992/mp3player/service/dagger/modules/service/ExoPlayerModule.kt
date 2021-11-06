package com.github.goldy1992.mp3player.service.dagger.modules.service

import com.github.goldy1992.mp3player.service.MyForwardingPlayer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ForwardingPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@InstallIn(ServiceComponent::class)
@Module
abstract class ExoPlayerModule {

    @Binds
    abstract fun bindsExoPlayer(simpleExoPlayer : SimpleExoPlayer) : ExoPlayer

    @Binds
    abstract fun bindsForwardingPlayer(myForwardingPlayer: MyForwardingPlayer) : ForwardingPlayer

    @Binds
    abstract fun bindsPlayer(player: ForwardingPlayer) : Player



}