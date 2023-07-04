package com.github.goldy1992.mp3player.client.models

import com.github.goldy1992.mp3player.commons.MediaItemType

data class RootChild
    constructor(
        override val id : String,
        override val type : MediaItemType,
    ) : MediaEntity