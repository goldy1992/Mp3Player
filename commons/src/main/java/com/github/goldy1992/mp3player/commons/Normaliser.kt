package com.github.goldy1992.mp3player.commons

import org.apache.commons.lang3.StringUtils

object Normaliser {
    @JvmStatic
    fun normalise(query: String): String {
        var query = query
        query = StringUtils.stripAccents(query)
        return query.trim { it <= ' ' }.toUpperCase()
    }
}