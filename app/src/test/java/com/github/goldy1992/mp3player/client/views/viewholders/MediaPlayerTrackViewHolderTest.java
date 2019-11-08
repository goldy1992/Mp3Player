package com.github.goldy1992.mp3player.client.views.viewholders;


import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.commons.MediaItemBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MediaPlayerTrackViewHolderTest {

    private MediaPlayerTrackViewHolder mediaPlayerTrackViewHolder;

    @Mock
    private AlbumArtPainter albumArtPainter;

    @Mock
    private TextView titleView;

    @Mock
    private TextView artistView;

    @Mock
    private ImageView albumArtView;

    @Mock
    private View view;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(view.findViewById(R.id.songTitle)).thenReturn(titleView);
        when(view.findViewById(R.id.songArtist)).thenReturn(artistView);
        when(view.findViewById(R.id.albumArt)).thenReturn(albumArtView);

        this.mediaPlayerTrackViewHolder = new MediaPlayerTrackViewHolder(view, albumArtPainter);
    }

    @Test
    public void testBindMediaItem() {
        final String expectedTitle = "TITLE";
        final String expectedArtist = "ARTIST";
        final Uri expectedAlbumArtUri = mock(Uri.class);

        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .setArtist(expectedArtist)
                .setAlbumArtUri(expectedAlbumArtUri)
                .build();

        MediaSessionCompat.QueueItem queueItem = new MediaSessionCompat.QueueItem(mediaItem.getDescription(), 1L);
        this.mediaPlayerTrackViewHolder.bindMediaItem(queueItem);

        verify(titleView, times(1)).setText(expectedTitle);
        verify(artistView, times(1)).setText(expectedArtist);
        verify(albumArtPainter, times(1)).paintOnView(albumArtView, expectedAlbumArtUri);
    }

    @Test
    public void testBindMediaItemByteAlbumArt() {
        final byte[] expectedAlbumArt = new byte[1];

        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle("title")
                .setArtist("artist")
                .setAlbumArtImage(expectedAlbumArt)
                .build();

        MediaSessionCompat.QueueItem queueItem = new MediaSessionCompat.QueueItem(mediaItem.getDescription(), 1L);
        this.mediaPlayerTrackViewHolder.bindMediaItem(queueItem);

        verify(albumArtPainter, times(1)).paintOnView(albumArtView, expectedAlbumArt);
    }
}