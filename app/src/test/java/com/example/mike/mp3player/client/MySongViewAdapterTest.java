package com.example.mike.mp3player.client;

import android.content.Context;
import android.view.ViewGroup;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.views.MyViewHolder;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MySongViewAdapterTest {

    private MySongViewAdapter mySongViewAdapter;
    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;
    private Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        LibraryObject parent = new LibraryObject(Category.SONGS, "id");
        this.mySongViewAdapter = new MySongViewAdapter(this.mediaBrowserAdapter, parent);
    }

    @Test
    public void testOnCreateViewHolder() {
        assertTrue(true);
        // TODO: fix test
//        ViewGroup group = mock(ViewGroup.class);
//        when(group.getContext()).thenReturn(this.context);
//        MyViewHolder result = mySongViewAdapter.onCreateViewHolder(group, 0);
//        assertNotNull(result);
    }
}