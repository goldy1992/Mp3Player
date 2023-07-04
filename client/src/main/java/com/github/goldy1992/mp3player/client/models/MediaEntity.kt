package com.github.goldy1992.mp3player.client.models

import com.github.goldy1992.mp3player.commons.MediaItemType

interface MediaEntity {
    val id : String
    val type : MediaItemType
}