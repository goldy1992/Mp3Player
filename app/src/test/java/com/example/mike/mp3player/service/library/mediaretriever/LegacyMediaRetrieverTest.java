package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class LegacyMediaRetrieverTest {

    private static final String LOG_TAG = "LGCY_MDIA_RTRVR_TST";

    private LegacyMediaRetriever legacyMediaRetriever;

    @Before
    public void setup() {
        final Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.legacyMediaRetriever = new LegacyMediaRetriever(context);
    }


    /**
     * TODO: in order to test, there needs to be an agreed directory that will always be readable
     * and writable in order to index.
     * @throws IOException IOException
     */
    @Test
    public void testRetrieveMedia() throws IOException {
        File rootDir = new File("rootDir");
        rootDir.mkdir();


        File mp3_1 = createFile(rootDir, "test1.mp3");
        File mp3_2 = createFile(rootDir, "test2.mp3");
        File noneMp3_1 = createFile(rootDir, "text.txt");
        File childDir = createDirectory(rootDir, "childDir");
        File wav_1 = createFile(childDir, "test4.wav");
        File noneMp3_2 = createFile(childDir, "noExtension");

        List<MediaBrowserCompat.MediaItem> resultList  = legacyMediaRetriever.retrieveMedia();
        noneMp3_2.delete();
        wav_1.delete();
        childDir.delete();
        noneMp3_1.delete();
        mp3_2.delete();
        mp3_1.delete();
        rootDir.delete();

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