package com.github.goldy1992.mp3player.commons

import org.apache.commons.lang3.StringUtils
import java.util.*

object Normaliser {
    @JvmStatic
    fun normalise(query: String): String {
        var toReturn = query
        toReturn = StringUtils.stripAccents(toReturn)
        return toReturn.trim { it <= ' ' }.toUpperCase(Locale.getDefault())
    }
}