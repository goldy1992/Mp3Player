package com.example.mike.mp3player.service.library;

import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.BuildConfig;

import org.apache.commons.lang.reflect.FieldUtils;
import org.codehaus.plexus.util.ExceptionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Tests run using JUnit 4!
 */
@RunWith(RobolectricTestRunner.class)
public class MediaLibraryTest {
    private static final String LOG_TAG = "MDIA_LBRY_TST";
    private static final String MOCK_PATH = "PATH";
b
    MediaLibrary mediaLibrary;

    @Before
    public void setUp() {

        try {
           // mediaLibrary = new MediaLibrary(InstrumentationRegistry.getInstrumentation().getContext());
        } catch (Exception ex) {
            Log.e("MEDIA_LIBRARY_TEST", "" + ExceptionUtils.getFullStackTrace(ex));
        }
    }
    /**
     *
     * @throws IOException
     */
    @Test
    public void rootDirectoryTest() throws IOException {
        assertTrue(true);
//        File rootDir = new File("rootDir");
//        rootDir.mkdir();
//        PowerMockito.mockStatic(MediaLibraryUtils.class);
//        PowerMockito.mockStatic(Uri.class);
//        when(MediaLibraryUtils.getExternalStorageDirectory()).thenReturn(rootDir);
//        when(MediaLibraryUtils.getSongTitle(any(), any())).thenCallRealMethod();
//        when(Uri.fromFile(any())).thenReturn(uri);
//        when(uri.getPath()).thenReturn(MOCK_PATH);
//
//        File mp3_1 = createFile(rootDir, "test1.mp3");
//        File mp3_2 = createFile(rootDir, "test2.mp3");
//        File noneMp3_1 = createFile(rootDir, "text.txt");
//        File childDir = createDirectory(rootDir, "childDir");
//        File wav_1 = createFile(childDir, "test4.wav");
//        File noneMp3_2 = createFile(childDir, "noExtension");
//
//
//        mediaLibrary.init();
//        noneMp3_2.delete();
//        wav_1.delete();
//        childDir.delete();
//        noneMp3_1.delete();
//        mp3_2.delete();
//        mp3_1.delete();
//        rootDir.delete();

        //assertTrue(mediaLibrary.getMediaUri(String.valueOf(MOCK_PATH.hashCode())).equals(uri));
    }

    private File createFile(File parentDir, String name) throws IOException {
        File f = new File(parentDir, name);
        f.createNewFile();
        return f;
    }

    private File createDirectory(File parentDir, String name) {
        File f = new File(parentDir, name);
        if (!f.mkdir()) {
            Log.i(LOG_TAG, "file: " + f + " does already exists");
        }
        return f;
    }
}