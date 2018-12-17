package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.net.Uri;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class MediaLibraryTest {

    private static final String MOCK_PATH = "PATH";

    @Mock
    Context context;
    @Mock
    Uri uri;

    MediaLibrary mediaLibrary;

    @BeforeEach
    public void setUp() {
        mediaLibrary = new MediaLibrary(context);
    }

    @AfterEach
    public void tearDown () {

    }

    /**
     *
     * @throws IOException
     */

    @Test
    public void rootDirectoryTest() throws IOException {
        File rootDir = new File("rootDir");
        rootDir.mkdir();

        File mp3_1 = createFile(rootDir, "test1.mp3");
        File mp3_2 = createFile(rootDir, "test2.mp3");
        File noneMp3_1 = createFile(rootDir, "text.txt");
        File childDir = createDirectory(rootDir, "childDir");
        File wav_1 = createFile(childDir, "test4.wav");
        File noneMp3_2 = createFile(childDir, "noExtension");

        mediaLibrary.init();
        noneMp3_2.delete();
        wav_1.delete();
        childDir.delete();
        noneMp3_1.delete();
        mp3_2.delete();
        mp3_1.delete();
        rootDir.delete();

        assertEquals(mediaLibrary.getMediaUri(String.valueOf(MOCK_PATH.hashCode())), uri);
    }

    private File createFile(File parentDir, String name) throws IOException {
        File f = new File(parentDir, name);
        f.createNewFile();
        return f;
    }

    private File createDirectory(File parentDir, String name) throws IOException {
        File f = new File(parentDir, name);
        f.mkdir();
        return f;
    }
}