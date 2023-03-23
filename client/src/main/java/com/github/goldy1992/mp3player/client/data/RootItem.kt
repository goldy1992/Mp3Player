package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.commons.MediaItemType

data class RootItem
    constructor(
        val id : String,
        val type : MediaItemType,
    ) : MediaEntity