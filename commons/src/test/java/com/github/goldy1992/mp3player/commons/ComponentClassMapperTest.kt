package com.github.goldy1992.mp3player.commons

import org.junit.Assert.*
import org.junit.Test

class ComponentClassMapperTest {

    @Test
    fun testBuildWithNoClasses() {
        val componentClassMapper : ComponentClassMapper = ComponentClassMapper.Builder().build()
        assertNull(componentClassMapper.folderActivity)
        assertNull(componentClassMapper.mainActivity)
        assertNull(componentClassMapper.mediaPlayerActivity)
        assertNull(componentClassMapper.searchResultActivity)
        assertNull(componentClassMapper.service)
    }

    @Test
    fun testBuildFolderActivity() {
        val mockFolderActivity : Class<*> = Object::class.java
        val componentClassMapper : ComponentClassMapper = ComponentClassMapper.Builder().
                folderActivity(mockFolderActivity)
                .build()
        assertEquals(mockFolderActivity, componentClassMapper.folderActivity)
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
    fun testBuildMediaPlayerActivity() {
        val mockMediaPlayerActivity : Class<*> = Object::class.java
        val componentClassMapper : ComponentClassMapper = ComponentClassMapper.Builder().
                mediaPlayerActivity(mockMediaPlayerActivity)
                .build()
        assertEquals(mockMediaPlayerActivity, componentClassMapper.mediaPlayerActivity)
    }

    @Test
    fun testBuildSearchResultActivity() {
        val mockSearchResultActivity : Class<*> = Object::class.java
        val componentClassMapper : ComponentClassMapper = ComponentClassMapper.Builder().
                searchResultActivity(mockSearchResultActivity)
                .build()
        assertEquals(mockSearchResultActivity, componentClassMapper.searchResultActivity)
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