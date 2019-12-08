package com.github.goldy1992.mp3player.service.library.utils

import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class MediaLibraryUtilsTest {
    private val PARENT_DIR = File.separator + "tmp"
    private val FILE = "test"
    private val EXPECTED_APPENDED_PATH = PARENT_DIR + File.separator + FILE
    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun testAppendToPath() {
        Assert.assertEquals(EXPECTED_APPENDED_PATH, MediaLibraryUtils.appendToPath(PARENT_DIR, FILE))
    }

    @Test
    fun testAppendToFilePath() {
        val originalFile = File(PARENT_DIR)
        val appendedFile = MediaLibraryUtils.appendToFilePath(originalFile, FILE)
        Assert.assertEquals(EXPECTED_APPENDED_PATH, appendedFile.path)
    }
}