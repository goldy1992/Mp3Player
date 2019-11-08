package com.github.goldy1992.mp3player.client.views.viewholders;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.utils.TimerUtils;
import com.github.goldy1992.mp3player.commons.MediaItemBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.github.goldy1992.mp3player.commons.Constants.UNKNOWN;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MySongViewHolderTest extends MediaItemViewHolderTestBase<MySongViewHolder> {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(view.findViewById(R.id.artist)).thenReturn(mock(TextView.class));
        when(view.findViewById(R.id.title)).thenReturn(mock(TextView.class));
        when(view.findViewById(R.id.duration)).thenReturn(mock(TextView.class));
        when(view.findViewById(R.id.song_item_album_art)).thenReturn(mock(ImageView.class));
        this.mediaItemViewHolder = spy(new MySongViewHolder(view, albumArtPainter));
    }

    @Test
    public void testBindMediaItemValidTitle() {
        final String expectedTitle = "title";
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .build();
        this.mediaItemViewHolder.bindMediaItem(mediaItem);
        verify(mediaItemViewHolder.getTitle(), times(1)).setText(expectedTitle);

    }
    @Test
    public void testBindMediaItemNullTitleNoFileName() {
        final String expectedTitle = UNKNOWN;
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle(null)
                .build();
        this.mediaItemViewHolder.bindMediaItem(mediaItem);
        verify(mediaItemViewHolder.getTitle(), times(1))
                .setText(expectedTitle);
    }

    @Test
    public void testExtractTitleNullTitleFileNameNoExtension() {
        final String expectedTitle = "no_extension";
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle(null)
                .setFileName(expectedTitle)
                .build();
        mediaItemViewHolder.bindMediaItem(mediaItem);
        verify(mediaItemViewHolder.getTitle(), times(1))
                .setText(expectedTitle);
    }
    @Test
    public void testExtractTitleNullTitleFileNameWithExtension() {
        final String expectedTitle = "file_with_extension";
        final String fileName =  expectedTitle + ".mp3";
                MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle(null)
                .setFileName(fileName)
                .build();
        mediaItemViewHolder.bindMediaItem(mediaItem);
        verify(mediaItemViewHolder.getTitle(), times(1))
                .setText(expectedTitle);
    }
    @Test
    public void testSetAlbumArt() {
        final Uri expectedUri = mock(Uri.class);
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setAlbumArtUri(expectedUri)
                .build();
        mediaItemViewHolder.bindMediaItem(mediaItem);
        verify(mediaItemViewHolder.albumArtPainter, times(1))
                .paintOnView(mediaItemViewHolder.getAlbumArt(), expectedUri);
    }
    @Test
    public void testSetArtist() {
        final String expectedArtist = "artist";
        MediaItem mediaItem = new MediaItemBuilder("id")
            .setArtist(expectedArtist)
            .build();
        mediaItemViewHolder.bindMediaItem(mediaItem);
        verify(mediaItemViewHolder.getArtist(), times(1))
                .setText(expectedArtist);
    }
    @Test
    public void testSetArtistNull() {
        final String expectedArtist = UNKNOWN;
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setArtist(null)
                .build();
        mediaItemViewHolder.bindMediaItem(mediaItem);
        verify(mediaItemViewHolder.getArtist(), times(1))
                .setText(expectedArtist);
    }
    @Test
    public void testSetDuration() {
        final long originalDuration = 34978L;
        final String expectedDuration = TimerUtils.formatTime(originalDuration);
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setDuration(originalDuration)
                .build();
        mediaItemViewHolder.bindMediaItem(mediaItem);
        verify(mediaItemViewHolder.getDuration(), times(1))
                .setText(expectedDuration);
    }
}