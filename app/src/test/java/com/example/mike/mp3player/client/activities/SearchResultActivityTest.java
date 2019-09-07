package com.example.mike.mp3player.client.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.appcompat.widget.SearchView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.commons.MediaItemBuilder;
import com.example.mike.mp3player.commons.MediaItemType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        assertNotNull(searchResultActivity);
    }

    @Test
    public void testOnItemSelected() {
        final SearchResultActivity searchResultActivitySpied = spy(searchResultActivity);
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder("id")
                .setMediaItemType(MediaItemType.SONGS)
                .build();
        searchResultActivitySpied.itemSelected(mediaItem);
        verify(searchResultActivitySpied, times(1)).startActivity(any());
    }

    @Test
    public void testHandleNewIntent() {
        SearchView spiedSearchView = spy(searchResultActivity.getSearchView());
        this.searchResultActivity.setSearchView(spiedSearchView);
        MediaBrowserAdapter mediaBrowserAdapterSpied = spy(searchResultActivity.mediaBrowserAdapter);
        this.searchResultActivity.setMediaBrowserAdapter(mediaBrowserAdapterSpied);
        final String expectedQuery = "QUERY";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, expectedQuery);
        searchResultActivity.onNewIntent(intent);
        verify(spiedSearchView, times(1)).setQuery(expectedQuery, false);
        verify(mediaBrowserAdapterSpied, times(1)).search(expectedQuery, null);
    }


}