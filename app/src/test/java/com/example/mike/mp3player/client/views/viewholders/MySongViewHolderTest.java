package com.example.mike.mp3player.client.views.viewholders;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.commons.MediaItemBuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MySongViewHolderTest extends MediaItemViewHolderTestBase {

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
    public void testBindMediaItem() {

        final String expectedTitle = "title";
        final String expectedArtist = "artist";
        final long originalDuration = 34978L;
        final String expectedDuration = TimerUtils.formatTime(originalDuration);
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .setArtist(expectedArtist)
                .setDuration(originalDuration)
                .build();
        this.mediaItemViewHolder.bindMediaItem(mediaItem);
        MySongViewHolder songViewHolder = (MySongViewHolder) mediaItemViewHolder;
        verify(songViewHolder.getTitle(), times(1)).setText(expectedTitle);
    }
}