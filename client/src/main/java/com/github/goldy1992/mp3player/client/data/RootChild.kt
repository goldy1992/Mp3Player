package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.commons.MediaItemType

data class RootChild
    constructor(
        override val id : String,
        val type : MediaItemType,
    ) : MediaEntity