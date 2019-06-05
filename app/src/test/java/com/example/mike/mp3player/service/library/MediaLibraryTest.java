package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MockMediaRetriever;

import org.codehaus.plexus.util.ExceptionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Set;

import static com.example.mike.mp3player.service.library.mediaretriever.MockMediaRetriever.NUM_OF_SONGS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests run using JUnit 4!
 */
@RunWith(RobolectricTestRunner.class)
public class MediaLibraryTest {
    private static final String LOG_TAG = "MDIA_LBRY_TST";
    private static final String MOCK_PATH = "PATH";
    private MediaLibrary mediaLibrary;
    private MockMediaRetriever mediaRetriever;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.mediaRetriever = new MockMediaRetriever(context);
        try {
            mediaLibrary = new MediaLibrary(context, mediaRetriever);
            mediaLibrary.buildMediaLibrary();
        } catch (Exception ex) {
            Log.e("MEDIA_LIBRARY_TEST", "" + ExceptionUtils.getFullStackTrace(ex));
        }
    }
    /**
     *
     */
    @Test
    public void testBuildMediaLibrary() {

        Set<Category> categories = mediaLibrary.getCategories().keySet();
        assertTrue(categories.contains(Category.FOLDERS));
        assertTrue(mediaLibrary.getCategories().get(Category.FOLDERS) instanceof FolderLibraryCollection);
        assertTrue(categories.contains(Category.SONGS));
        assertTrue(mediaLibrary.getCategories().get(Category.SONGS) instanceof SongCollection);
        assertTrue(categories.contains(Category.ROOT));
        assertTrue(mediaLibrary.getCategories().get(Category.ROOT) instanceof RootLibraryCollection);
    }

    @Test
    public void testGetPlaylistSongs() {
        LibraryObject libraryObject = new LibraryObject(Category.SONGS, "abc");
        List<MediaBrowserCompat.MediaItem> result = mediaLibrary.getPlaylist(libraryObject);
        assertNotNull(result);
        assertEquals(NUM_OF_SONGS, result.size());

    }
}