package com.github.goldy1992.mp3player.commons

import android.support.v4.media.MediaBrowserCompat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("rootitem")
class RootItem
    private constructor() {

    @JsonProperty
    private var id : String? = null
    @JsonProperty
    private var name : String? = null
    @JsonProperty
    private var description : String? = null

    class Builder() {
        private val toReturn : RootItem = RootItem()

        fun id(id : String?) : Builder {
            toReturn.id = id
            return this
        }

        fun name(name : String?) : Builder {
            toReturn.name = name
            return this
        }

        fun description(description : String?) : Builder {
            toReturn.description = description
            return this
        }

        fun build() : RootItem {
            return this.toReturn
        }
    }

    companion object {

        fun getRootItem(item: MediaBrowserCompat.MediaItem) : RootItem {
            return Builder()
                    .id(MediaItemUtils.getMediaId(item))
                    .name(MediaItemUtils.getTitle(item))
                    .build()
        }

    }
}