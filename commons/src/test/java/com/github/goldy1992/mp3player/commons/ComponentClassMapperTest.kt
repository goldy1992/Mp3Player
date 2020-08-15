package com.github.goldy1992.mp3player.commons

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ComponentClassMapperTest {

    @Test
    fun testBuildWithNoClasses() {
        val componentClassMapper : ComponentClassMapper = ComponentClassMapper.Builder().build()
        assertNull(componentClassMapper.mainActivity)
        assertNull(componentClassMapper.service)
    }

    @Test
    fun testBuildMainActivity() {
        val mockMainActivity : Class<*> = Object::class.java
        val componentClassMapper : ComponentClassMapper = ComponentClassMapper.Builder().
                mainActivity(mockMainActivity)
                .build()
        assertEquals(mockMainActivity, componentClassMapper.mainActivity)
    }

    @Test
    fun testBuildService() {
        val mockService : Class<*> = Object::class.java
        val componentClassMapper : ComponentClassMapper = ComponentClassMapper.Builder().
                service(mockService)
                .build()
        assertEquals(mockService, componentClassMapper.service)
    }

}