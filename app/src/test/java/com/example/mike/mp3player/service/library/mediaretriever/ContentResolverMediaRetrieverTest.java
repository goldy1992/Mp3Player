package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaMetadataCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.RoboCursor;
import org.robolectric.shadows.ShadowContentResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class ContentResolverMediaRetrieverTest {

    private ContentResolverMediaRetriever mediaRetriever;
    private RoboCursor cursor;
    private List<Map<String, Object>> testData;

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
        this.cursor.setResults(setupTestData());
        List<MediaItem> result = mediaRetriever.retrieveMedia();

        for (int i = 0; i < testData.size(); i++) {
            MediaItem item = result.get(i);
            Bundle extras = item.getDescription().getExtras();

            // assert duration
            String duration = extras.getString(MediaMetadataCompat.METADATA_KEY_DURATION);
            assertEquals(testData.get(i).get(Media.DURATION), duration);

            // assert title
            String title = item.getDescription().getTitle().toString();
            assertEquals(testData.get(i).get(Media.TITLE), title);

            // assert artist
            String artist = extras.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
            assertEquals(testData.get(i).get(Media.ARTIST), artist);

        }
    }

    @SuppressWarnings("unchecked")
    private Object[][] setupTestData() {
        this.testData = new ArrayList<>();
        Map projectionArray1 = new TestDataBuilder()
                .path("path1")
                .duration("735443")
                .artist("artist1")
                .title("title1")
                .album("album1")
                .albumId(100L)
                .album("album1")
                .build();
        testData.add(projectionArray1);

        Map projectionArray2 = new TestDataBuilder()
                .path("path2")
                .duration("923383")
                .artist("artist2")
                .title("title2")
                .album("album2")
                .albumId(101L)
                .album("album2")
                .build();
        testData.add(projectionArray2);

        final int arraySize = ContentResolverMediaRetriever.getProjection().length;

        List<Object[]> objects = new ArrayList<>();
        Object[][] testDataArray = new Object[2][arraySize];
        for (int i = 0; i < 2; i++) {
            Map m = testData.get(i);
            List<Object> mapData = new ArrayList<>();
            for (String projection : ContentResolverMediaRetriever.getProjection()) {
                mapData.add(m.get(projection));
            }
            objects.add(mapData.toArray());
            testDataArray[i] = mapData.toArray();
        }
        return testDataArray;
    }


    class TestDataBuilder {

        private Map<String, Object> objectMap;

        TestDataBuilder() {
            this.objectMap = new HashMap<>();
        }

        TestDataBuilder path(String path) {
            objectMap.put(Media.DATA, path);
            return this;
        }

        TestDataBuilder duration(String duration) {
            objectMap.put(Media.DURATION, duration);
            return this;
        }

        TestDataBuilder artist(String artist) {
            objectMap.put(Media.ARTIST, artist);
            return this;
        }

        TestDataBuilder title(String title) {
            objectMap.put(Media.TITLE, title);
            return this;
        }

        TestDataBuilder album(String album) {
            objectMap.put(Media.ALBUM, album);
            return this;
        }

        TestDataBuilder albumId(long albumId) {
            objectMap.put(Media.ALBUM_ID, albumId);
            return this;
        }

        Map build() {
            return objectMap;
        }

    }

}