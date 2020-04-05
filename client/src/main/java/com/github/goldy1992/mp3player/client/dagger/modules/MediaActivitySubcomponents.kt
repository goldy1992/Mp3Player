package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.dagger.subcomponents.*
import dagger.Module

@Module(subcomponents = [
    MediaItemListFragmentSubcomponent::class,
    MediaFragmentSubcomponent::class
])
class MediaActivitySubcomponents