package com.example.mike.mp3player.server.library;

import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(MediaLibraryUtils.class)
public class MediaLibraryTest {

    MediaLibrary mediaLibrary = new MediaLibrary();

    @Before
    public void setUp() {
        mediaLibrary.init();
    }

    @After
    public void tearDown () {

    }

    @Test
    public void rootDirectoryTest() throws IOException {
        File rootDir = new File("rootDir");
        rootDir.mkdir();

        PowerMockito.mockStatic(MediaLibraryUtils.class);
        when(MediaLibraryUtils.getExternalStorageDirectory()).thenReturn(rootDir);

        File mp3_1 = createFile(rootDir, "test1.mp3");
        File mp3_2 = createFile(rootDir, "test2.mp3");
        File noneMp3_1 = createFile(rootDir, "text.txt");
        File childDir = createDirectory(rootDir, "childDir");
        File wav_1 = createFile(childDir, "test4.wav");
        File noneMp3_2 = createFile(childDir, "noExtension");


        mediaLibrary.buildMediaLibrary();
        noneMp3_2.delete();
        wav_1.delete();
        childDir.delete();
        noneMp3_1.delete();
        mp3_2.delete();
        mp3_1.delete();
        rootDir.delete();

//        assertTrue(mediaLibrary.getLibrary().keySet().contains(rootDir));
  //      assertTrue(mediaLibrary.getLibrary().keySet().contains(childDir));
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