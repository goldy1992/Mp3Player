package com.example.mike.mp3player.client.views.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.views.viewholders.MyFolderViewHolder;
import com.example.mike.mp3player.client.views.viewholders.MySongViewHolder;
import com.example.mike.mp3player.client.views.viewholders.RootItemViewHolder;
import com.example.mike.mp3player.commons.MediaItemType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SearchResultAdapterTest extends RecyclerViewAdapterTestBase{

    private SearchResultAdapter searchResultAdapter;

    @Mock
    private AlbumArtPainter albumArtPainter;

    private Context context;
    @Before
    public void setup() {
        super.setup();
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.searchResultAdapter = new SearchResultAdapter(albumArtPainter);
    }

    @Test
    public void testOnCreateViewHolderRootItem() {
        assertCreatedViewItem(RootItemViewHolder.class, MediaItemType.ROOT.getValue());
    }


    @Test
    public void testOnFolderViewHolderSongItem() {
        assertCreatedViewItem(MySongViewHolder.class, MediaItemType.SONG.getValue());
    }

    @Test
    public void testOnFolderViewHolderFolderItem() {
        assertCreatedViewItem(MyFolderViewHolder.class, MediaItemType.FOLDER.getValue());
    }



    private <T extends RecyclerView.ViewHolder> void assertCreatedViewItem(Class<T> viewHolderType, int viewType) {
        RecyclerView.ViewHolder viewHolder =
                searchResultAdapter.onCreateViewHolder(this.viewGroup, viewType);
        assertEquals(viewHolder.getClass(), viewHolderType);
    }

}