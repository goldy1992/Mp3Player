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
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class ContentResolverMediaRetrieverTest {

    private ContentResolverMediaRetriever mediaRetriever;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.mediaRetriever = new ContentResolverMediaRetriever(context);


    }

    @Test
    public void testQuery() {
        RoboCursor roboCursor = new RoboCursor();
        roboCursor.projection = ContentResolverMediaRetriever.getProjection();
        roboCursor.setColumnNames(Arrays.asList(roboCursor.projection));
     //   roboCursor.setResults();

        ShadowContentResolver shadowContentResolver = shadowOf(mediaRetriever.getContentResolver());
        shadowContentResolver.setCursor(new RoboCursor());
        List<MediaItem> result = mediaRetriever.retrieveMedia();
        assertNotNull(result);
    }

    private Object[][] testData() {
        return null;
    }

}