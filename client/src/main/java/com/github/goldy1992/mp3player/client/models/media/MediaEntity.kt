package com.github.goldy1992.mp3player.client.models.media

import com.github.goldy1992.mp3player.commons.MediaItemType

/**
 * Interface which all UI models should extend.
 */
interface MediaEntity {
    /**
     * The id of the [MediaEntity].
     */
    val id : String

    /**
     * THe [MediaItemType].
     */
    val type : MediaItemType

    /**
     * The load [State] of the object.
     */
    val state : State
}