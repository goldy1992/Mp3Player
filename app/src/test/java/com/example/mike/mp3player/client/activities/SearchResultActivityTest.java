package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class SearchResultActivityTest {

    /** Intent */
    private Intent intent;
    /** Activity controller */
    ActivityController<SearchResultActivityInjectorTestImpl> activityController;
    private SearchResultActivity searchResultActivity;
    private MediaSessionCompat mediaSessionCompat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        mediaSessionCompat = new MediaSessionCompat(context, "TAG");
        this.intent = new Intent(ApplicationProvider.getApplicationContext(), MediaPlayerActivity.class);
        this.activityController = Robolectric.buildActivity(SearchResultActivityInjectorTestImpl.class, intent).setup();
        this.searchResultActivity = activityController.get();
    }

    @Test
    public void testInitialisation() {
        assertNotNull(searchResultActivity);
    }


}