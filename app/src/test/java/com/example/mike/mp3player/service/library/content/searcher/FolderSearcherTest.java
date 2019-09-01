package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.service.library.content.parser.FolderResultsParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.sql.SQLException;

@RunWith(RobolectricTestRunner.class)
public class FolderSearcherTest {

    private FolderSearcher folderSearcher;
    private ContentResolver contentResolver;

    @Before
    public void setup() throws SQLException {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.contentResolver = context.getContentResolver();
        this.folderSearcher = new FolderSearcher(contentResolver, new FolderResultsParser(), "");
    }

    @Test
    public void firstTest() {

    }


}