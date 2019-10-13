package com.example.mike.mp3player.client.views.adapters;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.views.viewholders.EmptyListViewHolder;
import com.example.mike.mp3player.client.views.viewholders.MyFolderViewHolder;
import com.example.mike.mp3player.client.views.viewholders.MySongViewHolder;
import com.example.mike.mp3player.client.views.viewholders.RootItemViewHolder;
import com.example.mike.mp3player.commons.MediaItemBuilder;
import com.example.mike.mp3player.commons.MediaItemType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class SearchResultAdapterTest extends MediaItemRecyclerViewAdapterTestBase {

    private SearchResultAdapter searchResultAdapter;

    @Mock
    private Handler handler;

    @Before
    public void setup() {
        super.setup();
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.searchResultAdapter = new SearchResultAdapter(albumArtPainter, handler);
    }
    /**
     * Test creation of RootItemViewHolder
     */
    @Test
    public void testOnCreateViewHolderRootItem() {
        assertCreatedViewItem(RootItemViewHolder.class, MediaItemType.ROOT.getValue());
    }
    /**
     * Test creation of SongItemViewHolder
     */
    @Test
    public void testOnFolderViewHolderSongItem() {
        assertCreatedViewItem(MySongViewHolder.class, MediaItemType.SONG.getValue());
    }

    @Test
    public void testOnEmptyViewHolderSongItem() {
        assertCreatedViewItem(EmptyListViewHolder.class, -1);
    }
    /**
     * Test creation of FolderItemViewHolder
     */
    @Test
    public void testOnFolderViewHolderFolderItem() {
        assertCreatedViewItem(MyFolderViewHolder.class, MediaItemType.FOLDER.getValue());
    }
    /** */
    @Test
    public void testBindViewHolder() {
        MySongViewHolder mySongViewHolder = mock(MySongViewHolder.class);
        this.mediaItems.add(
                new MediaItemBuilder("101")
                        .build()
        );
        this.searchResultAdapter.getItems().addAll(mediaItems);
        searchResultAdapter.onBindViewHolder(mySongViewHolder, 0);
    }
    @Test
    public void testGetItemViewType() {
        final MediaItemType mediaItemType = MediaItemType.FOLDER;
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setMediaItemType(mediaItemType)
                .build();
        this.searchResultAdapter.getItems().add(mediaItem); // add as the first item
        int result = searchResultAdapter.getItemViewType(0);
        assertEquals(mediaItemType.getValue(), result);

    }
    @Test
    public void testGetItemViewTypeNoMediaItemType() {
        MediaItem mediaItem = new MediaItemBuilder("id")
                .build();
        this.searchResultAdapter.getItems().add(mediaItem); // add as the first item
        int result = searchResultAdapter.getItemViewType(0);
        assertEquals(0, result);
    }

    @Test
    public void testItemCount() {
        MediaItem mediaItem = mock(MediaItem.class);
        final int expectedSize = 5;
        for (int i = 1; i <= 5; i++) {
            mediaItems.add(mediaItem);
        }
        searchResultAdapter.getItems().addAll(mediaItems);
        assertEquals(expectedSize, searchResultAdapter.getItemCount());
    }
    /**
     *
     * @param viewHolderType the view holder class type
     * @param viewType the view type code
     * @param <T> The type of ViewHolder
     */
    private <T extends RecyclerView.ViewHolder> void assertCreatedViewItem(Class<T> viewHolderType, int viewType) {
        RecyclerView.ViewHolder viewHolder =
                searchResultAdapter.onCreateViewHolder(this.viewGroup, viewType);
        assertEquals(viewHolder.getClass(), viewHolderType);
    }

}