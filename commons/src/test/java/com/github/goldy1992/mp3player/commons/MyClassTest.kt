package com.github.goldy1992.mp3player.commons

import com.github.goldy1992.mp3player.commons.MyClass
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyClassTest {

    @Test
    fun testAddSomething() {
        val a = 9
        val b = 8
        val expected = 17
        val toTest = MyClass()
        val result = toTest.addSomething(a, b)
        assertEquals(expected, result)
    }
}