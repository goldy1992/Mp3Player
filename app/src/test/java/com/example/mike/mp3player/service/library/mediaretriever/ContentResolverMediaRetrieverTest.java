package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.BaseCursor;
import org.robolectric.fakes.RoboCursor;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowContentResolver;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class ContentResolverMediaRetrieverTest {

    private ContentResolverMediaRetriever mediaRetriever;
    private RoboCursor cursor;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.mediaRetriever = new ContentResolverMediaRetriever(context);
        this.cursor = new RoboCursor();
        cursor.projection = ContentResolverMediaRetriever.getProjection();
        cursor.setColumnNames(Arrays.asList(cursor.projection));
        ShadowContentResolver shadowContentResolver = shadowOf(mediaRetriever.getContentResolver());
        shadowContentResolver.setCursor(cursor);
    }

    @Test
    public void testEmptyCursorGivesEmptyResult() {
        List<MediaItem> result = mediaRetriever.retrieveMedia();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPopulatedQuery() {
        this.cursor.setResults(testData());
        List<MediaItem> result = mediaRetriever.retrieveMedia();
        assertNotNull(result);
    }

    private Object[][] testData() {
        Object[] val1 = {"path1", "735443", "artist1", "title1", "album1", 100L};
        Object[] val2 = {"path2", "923383", "artist2", "title2", "album2", 101L};
        Object[][] testData = {val1, val2};
        return testData;
    }

}