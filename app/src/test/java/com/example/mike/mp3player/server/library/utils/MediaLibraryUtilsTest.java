package com.example.mike.mp3player.server.library.utils;

import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;

public class MediaLibraryUtilsTest {

    private final String PARENT_DIR = File.separator + "tmp";
    private final String FILE = "test";
    private final String EXPECTED_APPENDED_PATH = PARENT_DIR + File.separator + FILE;

    @Before
    public void setUp() {
    }

    @After
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
