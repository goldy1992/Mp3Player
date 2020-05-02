package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaFragmentSubcomponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaItemListFragmentSubcomponent
import dagger.Module

@Module(subcomponents = [
    MediaItemListFragmentSubcomponent::class,
    MediaFragmentSubcomponent::class
])
class MediaActivitySubcomponents