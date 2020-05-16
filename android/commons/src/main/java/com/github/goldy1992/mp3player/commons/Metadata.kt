package com.github.goldy1992.mp3player.commons

import android.support.v4.media.MediaBrowserCompat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("metadata")
class Metadata
    private constructor()
{

    @JsonProperty
    private var id : String? = null
    @JsonProperty
    private var title : String? = Constants.UNKNOWN
    @JsonProperty
    private var artist : String? = Constants.UNKNOWN
    @JsonProperty
    private var duration : Long? = 0L
    @JsonProperty
    private var albumArtPath : String? = null

    class Builder() {
        private val toReturn : Metadata = Metadata()

        fun id(id : String?) : Builder {
            toReturn.id = id
            return this
        }

        fun title(title : String?) : Builder {
            toReturn.title = title
            return this
        }

        fun artist(artist : String?) : Builder {
            toReturn.artist = artist
            return this
        }

        fun duration(duration : Long) : Builder {
            toReturn.duration = duration
            return this
        }

        fun albumArtPath(albumArtPath : String) : Builder {
            toReturn.albumArtPath = albumArtPath
            return this
        }

        fun build() : Metadata {
            return this.toReturn
        }
    }

    companion object {

        fun getMetadata(item: MediaBrowserCompat.MediaItem) : Metadata {
            return Builder()
                    .id(MediaItemUtils.getMediaId(item))
                    .title(MediaItemUtils.getTitle(item))
                    .artist(MediaItemUtils.getArtist(item))
                    .duration(MediaItemUtils.getDuration(item))
                    .build()
        }

    }
}