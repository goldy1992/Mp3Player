package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.service.library.content.filter.FoldersResultFilter;
import com.example.mike.mp3player.service.library.content.parser.FolderResultsParser;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FolderSearcherTest {

    private FolderSearcher folderSearcher;
    private ContentResolver contentResolver;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.contentResolver = context.getContentResolver();
        this.folderSearcher = new FolderSearcher(contentResolver, new FolderResultsParser(), "", new FoldersResultFilter());
    }


}