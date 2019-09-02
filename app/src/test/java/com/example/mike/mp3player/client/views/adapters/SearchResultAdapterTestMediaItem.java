package com.example.mike.mp3player.client.views.adapters;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.views.viewholders.MyFolderViewHolder;
import com.example.mike.mp3player.client.views.viewholders.MySongViewHolder;
import com.example.mike.mp3player.client.views.viewholders.RootItemViewHolder;
import com.example.mike.mp3player.commons.MediaItemBuilder;
import com.example.mike.mp3player.commons.MediaItemType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class SearchResultAdapterTestMediaItem extends MediaItemRecyclerViewAdapterTestBase {

    private SearchResultAdapter searchResultAdapter;

    @Before
    public void setup() {
        super.setup();
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.searchResultAdapter = new SearchResultAdapter(albumArtPainter);
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
    /**
     * Test creation of FolderItemViewHolder
     */
    @Test
    public void testOnFolderViewHolderFolderItem() {
        assertCreatedViewItem(MyFolderViewHolder.class, MediaItemType.FOLDER.getValue());
    }
    /**
     *
     */
    @Test
    public void testBindSongViewHolder() {
        MySongViewHolder mySongViewHolder = mock(MySongViewHolder.class);
        this.mediaItems.add(
                new MediaItemBuilder("101")
                        .setDescription("description1")
                        .setMediaItemType(MediaItemType.SONG)
                        .setDuration(45646L)
                        .setArtist("art")
                        .build()
        );
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