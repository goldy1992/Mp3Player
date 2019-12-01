package com.github.goldy1992.mp3player.client.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.appcompat.widget.SearchView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.goldy1992.mp3player.client.MediaBrowserAdapter;
import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.commons.MediaItemType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
    public void testOnSongItemSelected() {
        final SearchResultActivity searchResultActivitySpied = spy(searchResultActivity);
        final MediaControllerAdapter spiedMediaControllerAdapter = spy(searchResultActivitySpied.mediaControllerAdapter);
        searchResultActivitySpied.mediaControllerAdapter = spiedMediaControllerAdapter;
        final String libraryId = "libId";
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder("id")
                .setMediaItemType(MediaItemType.SONGS)
                .setLibraryId(libraryId)
                .build();
        searchResultActivitySpied.itemSelected(mediaItem);
        verify(searchResultActivitySpied, never()).startActivity(any());
        verify(spiedMediaControllerAdapter, times(1)).playFromMediaId(libraryId, null);
    }

    @Test
    public void testOnFolderItemSelected() {
        final SearchResultActivity searchResultActivitySpied = spy(searchResultActivity);
        final MediaControllerAdapter spiedMediaControllerAdapter = spy(searchResultActivitySpied.mediaControllerAdapter);
        searchResultActivitySpied.mediaControllerAdapter = spiedMediaControllerAdapter;
        final String libraryId = "libId";
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder("id")
                .setMediaItemType(MediaItemType.FOLDERS)
                .setLibraryId(libraryId)
                .build();
        searchResultActivitySpied.itemSelected(mediaItem);
        verify(searchResultActivitySpied, times(1)).startActivity(any());
        verify(spiedMediaControllerAdapter, never()).playFromMediaId(libraryId, null);
    }

    @Test
    public void testHandleNewIntent() {
        SearchView spiedSearchView = spy(searchResultActivity.searchView);
        this.searchResultActivity.searchView = spiedSearchView;
        MediaBrowserAdapter mediaBrowserAdapterSpied = spy(searchResultActivity.mediaBrowserAdapter);
        this.searchResultActivity.mediaBrowserAdapter = mediaBrowserAdapterSpied;
        final String expectedQuery = "QUERY";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, expectedQuery);
        searchResultActivity.onNewIntent(intent);
        verify(spiedSearchView, times(1)).setQuery(expectedQuery, false);
        verify(mediaBrowserAdapterSpied, times(1)).search(expectedQuery, null);
    }


}