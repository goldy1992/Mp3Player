package com.github.goldy1992.mp3player.commons


import androidx.media3.common.MediaItem
import java.util.*

object ComparatorUtils {

    object Companion {
        private const val LOG_TAG = "COMPARATOR_UTILS"

        @JvmField
        val compareRootMediaItemsByMediaItemType: Comparator<MediaItem> = Comparator<MediaItem> { m1, m2 ->
            val c1 = MediaItemUtils.getRootMediaItemType(m1)
            val c2 = MediaItemUtils.getRootMediaItemType(m2)
            if (c1 == null && c2 == null) {
                0
            } else if (c1 == null) {
                -1
            } else if (c2 == null) {
                1
            } else {
                c1.rank - c2.rank
            }
        }
        @JvmStatic
        val compareMediaItemsByTitle: Comparator<MediaItem> = Comparator<MediaItem> { m1, m2 ->
            uppercaseStringCompare.compare(MediaItemUtils.getTitle(m1), MediaItemUtils.getTitle(m2))
        }
        @JvmStatic
        val compareMediaItemById = Comparator<MediaItem> { m1, m2 ->
            val id1 = MediaItemUtils.getMediaId(m1)
            val id2 = MediaItemUtils.getMediaId(m2)
            id1.compareTo(id2)
        }
        @JvmStatic
        val uppercaseStringCompare = Comparator<String> { string1, string2 ->
            if (string1 == null && string2 == null) {
                0
            } else if (null == string1) {
                -1
            } else if (null == string2) {
                1
            } else {
                string1.uppercase(Locale.ROOT).compareTo(string2.uppercase(Locale.ROOT))
            }
        }
        @JvmField
        val caseSensitiveStringCompare: Comparator<String> = Comparator<String> { string1, string2 ->
            if (string1 == null && string2 == null) {
                0
            } else if (null == string1) {
                -1
            } else if (null == string2) {
                1
            } else {
                string1.compareTo(string2)
            }
        }
    }
}