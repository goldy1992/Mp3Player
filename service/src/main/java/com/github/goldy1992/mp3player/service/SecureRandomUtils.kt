package com.github.goldy1992.mp3player.service

import org.apache.commons.lang3.RandomStringUtils
import java.security.SecureRandom

object SecureRandomUtils {

    /**
     * Generates a random alphanumeric string using a [SecureRandom] object.
     * @return An alphanumeric number of size count.
     */
    fun randomAlphaNumeric(count : Int) : String {
        val secureRandom = SecureRandom()
        return RandomStringUtils.random(count, 0, 0, true, true,null, secureRandom)
    }
}