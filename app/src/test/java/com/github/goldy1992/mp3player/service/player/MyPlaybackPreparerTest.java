package com.github.goldy1992.mp3player.service.player;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.session.PlaybackStateCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.service.MyControlDispatcher;
import com.github.goldy1992.mp3player.service.PlaybackManager;
import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.FileDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MyPlaybackPreparerTest {

    @Mock
    private ContentManager contentManager;
    @Mock
    private ExoPlayer exoPlayer;
    @Mock
    private MediaSourceFactory mediaSourceFactory;
    @Mock
    private MyControlDispatcher myControlDispatcher;
    @Mock
    private PlaybackManager playbackManager;
    @Mock
    private MediaSource mediaSource;

    private MyPlaybackPreparer myPlaybackPreparer;

    @Before
    public void setup() throws FileDataSource.FileDataSourceException {
        MockitoAnnotations.initMocks(this);
        MediaItem testItem = new MediaItemBuilder("id1").setMediaUri(Uri.parse("string")).build();
        List<MediaItem> items = Collections.singletonList(testItem);
        when(mediaSourceFactory.createMediaSource(any())).thenReturn(mediaSource);
        this.myPlaybackPreparer = new MyPlaybackPreparer(exoPlayer, contentManager, items, mediaSourceFactory, myControlDispatcher, playbackManager);
    }

    @Test
    public void testSupportedActions() {
        List<MediaItem> items = new ArrayList<>();
        myPlaybackPreparer = new MyPlaybackPreparer(exoPlayer, contentManager, items, mediaSourceFactory, myControlDispatcher, playbackManager);
        assertContainsAction(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        assertContainsAction(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
    }

    /**
     * When the PlaybackPreparer is created then items are prepared, but not played
     * @throws FileDataSource.FileDataSourceException
     */
    @Test
    public void testPreparePlaylistOnConstruct() throws FileDataSource.FileDataSourceException {
        // don't play when being constructed
        verify(myControlDispatcher, times(1)).dispatchSetPlayWhenReady(exoPlayer, false);
        // should seek to the first index, position 0
        verify(myControlDispatcher, times(1)).dispatchSeekTo(exoPlayer, 0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOnPrepareFromSearch() {
        myPlaybackPreparer.onPrepareFromSearch("query", true, null);
    }

    @Test
    public void testOnPrepareFromUri() {
        Uri testUri = Uri.parse("query");
        MediaItem testItem = new MediaItemBuilder("id1").setMediaUri(Uri.parse("string")).build();
        when(contentManager.getItem(testUri)).thenReturn(testItem);

        myPlaybackPreparer.onPrepareFromUri(testUri, true, null);
        verify(playbackManager, times(1)).createNewPlaylist(any());
    }

    @Test
    public void testOnCommand() {
        assertFalse(myPlaybackPreparer.onCommand(exoPlayer, mock(ControlDispatcher.class), "query", null, null));
    }

    @Test
    public void testPrepareFromMediaId() {
        final String trackId = "id2";
        final String mediaId = "x|" + trackId;
        MediaItem testItem1 = new MediaItemBuilder("id1").setMediaUri(Uri.parse("string")).build();
        MediaItem testItem2 = new MediaItemBuilder(trackId).setMediaUri(Uri.parse("string")).build();
        MediaItem testItem3 = new MediaItemBuilder("id3").setMediaUri(Uri.parse("string")).build();
        List<MediaItem> items = new ArrayList<>();
        items.add(testItem1);
        items.add(testItem2);
        items.add(testItem3);
        when(contentManager.getPlaylist(mediaId)).thenReturn(items);
        myPlaybackPreparer.onPrepareFromMediaId(mediaId, true, null);
        verify(myControlDispatcher, times(1)).dispatchSeekTo(exoPlayer, 1, 0);
    }

    private void assertContainsAction(@PlaybackStateCompat.Actions long action) {
        final long actions = myPlaybackPreparer.getSupportedPrepareActions();
        final boolean containsAction = 0 != (actions & action);
        assertTrue(containsAction);
    }
}