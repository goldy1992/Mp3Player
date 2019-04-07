package com.example.mike.mp3player.service.library.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class MediaLibraryUtilsTest {

    private final String PARENT_DIR = File.separator + "tmp";
    private final String FILE = "test";
    private final String EXPECTED_APPENDED_PATH = PARENT_DIR + File.separator + FILE;

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAppendToPath() {
        assertEquals(EXPECTED_APPENDED_PATH, MediaLibraryUtils.appendToPath(PARENT_DIR, FILE) );
    }

    @Test
    public void testAppendToFilePath() {
        File originalFile = new File(PARENT_DIR);
        File appendedFile = MediaLibraryUtils.appendToFilePath(originalFile, FILE);

        assertEquals(EXPECTED_APPENDED_PATH, appendedFile.getPath());
    }
}
