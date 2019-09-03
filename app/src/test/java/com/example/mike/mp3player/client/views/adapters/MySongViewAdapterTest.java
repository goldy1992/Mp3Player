package com.example.mike.mp3player.client.views.adapters;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.views.viewholders.MySongViewHolder;
import com.example.mike.mp3player.commons.MediaItemBuilder;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static com.example.mike.mp3player.TestUtils.createMediaItem;
import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MySongViewAdapterTest extends MediaItemRecyclerViewAdapterTestBase {

    private MySongViewAdapter mySongViewAdapter;
    @Captor
    private ArgumentCaptor<MediaItem> captor;
    @Mock
    private MySongViewHolder mySongViewHolder;

    @Before
    public void setup() {
        super.setup();
        MockitoAnnotations.initMocks(this);
        this.mySongViewAdapter = new MySongViewAdapter(albumArtPainter);
    }

    @Test
    public void testOnCreateViewHolder() {

        MySongViewHolder result = (MySongViewHolder) mySongViewAdapter.onCreateViewHolder(this.viewGroup, 0);
        assertNotNull(result);
    }

    @Test
    public void testOnBindViewHolderEmptyValues() {
        final String expectedArtist = UNKNOWN;
        final String expectedTitle = "";
        this.mediaItems.add(
                new MediaItemBuilder("101")
                .setDescription("description1")
                .setMediaItemType(MediaItemType.SONG)
                .setDuration(45646L)
                .setTitle(expectedTitle)
                .setArtist(expectedArtist)
                .build()
        );
        this.mySongViewAdapter.notifyDataSetChanged();
        bindViewHolder();
        verify(mySongViewHolder, times(1)).bindMediaItem(captor.capture());
        MediaItem result = captor.getValue();
        assertEquals(expectedArtist, MediaItemUtils.getArtist(result));
        assertEquals(expectedTitle, MediaItemUtils.getTitle(result));
    }

    @Test
    public void testBindNullTitleUseFileName() {
        final String fileName = "FILE_NAME";
        final String extension = ".mp3";
        final String fullFileName = fileName + extension;
        MediaItem mediaItem = createMediaItem("ID", null, "description", MediaItemType.ROOT, 32525L);
        mediaItem.getDescription().getExtras().putString(META_DATA_KEY_FILE_NAME, fullFileName);
        this.mediaItems.add(mediaItem);
        bindViewHolder();
        verify(mySongViewHolder, times(1)).bindMediaItem(captor.capture());
        MediaItem result = captor.getValue();
        assertNull(MediaItemUtils.getTitle(result));
    }

    @Test
    public void testOnBindViewHolder() {
        // TODO: refactor to have an OnBindViewHolder setup method and test for different list indices
        final String expectedArtist = "artist";
        final long originalDuration = 34234L;
        final String expectedDuration = TimerUtils.formatTime(Long.valueOf(originalDuration));
        final String expectedTitle = "title";
        mediaItems.add(
            new MediaItemBuilder("101")
            .setTitle(expectedTitle)
            .setDescription("description1")
            .setMediaItemType(MediaItemType.ROOT)
            .setDuration(originalDuration)
            .setArtist(expectedArtist)
            .build());
        mediaItems.add(
            new MediaItemBuilder("102")
            .setTitle("title2")
            .setDescription("description2")
            .setMediaItemType(MediaItemType.ROOT)
            .setDuration(34234L)
            .build());

        bindViewHolder();
        verify(mySongViewHolder, times(1)).bindMediaItem(captor.capture());
        MediaItem result = captor.getValue();
        assertEquals(expectedArtist, MediaItemUtils.getArtist(result));
        assertEquals(expectedTitle, MediaItemUtils.getTitle(result));
        assertEquals(expectedDuration, MediaItemUtils.getDuration(result));
    }

    private void bindViewHolder() {
        mySongViewAdapter.setItems(mediaItems);
        mySongViewAdapter.onBindViewHolder(mySongViewHolder, 0);
    }
}