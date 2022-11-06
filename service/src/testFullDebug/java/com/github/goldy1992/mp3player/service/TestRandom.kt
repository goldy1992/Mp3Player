package com.github.goldy1992.mp3player.service

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test
import java.security.SecureRandom

class TestRandom {

    @Test
    fun testRandom() {
        val random = SecureRandom()
        val result = RandomStringUtils.random(15, 0, 0, true, true,null, random)
        println(result)
    }
}