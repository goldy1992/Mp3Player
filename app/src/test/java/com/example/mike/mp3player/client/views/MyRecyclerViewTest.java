package com.example.mike.mp3player.client.views;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MyFolderItemTouchListener;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.MySongItemTouchListener;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class MyRecyclerViewTest {

    private MyRecyclerView myRecyclerView;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.myRecyclerView = new MyRecyclerView(context);
    }

    @Test
    public void testInitRecyclerViewForSongs() {
        LibraryObject parent = new LibraryObject(Category.ROOT, "id");
        MediaBrowserAdapter mediaBrowserAdapter = mock(MediaBrowserAdapter.class);
        Category category = Category.SONGS;
        MyGenericItemTouchListener.ItemSelectedListener listener = mock(MyGenericItemTouchListener.ItemSelectedListener.class);
        myRecyclerView.initRecyclerView(category, listener);
        RecyclerView.Adapter adapter = myRecyclerView.getAdapter();
        assertNotNull(myRecyclerView.getAdapter());
        assertTrue(adapter instanceof MySongViewAdapter);
        MyGenericItemTouchListener touchListener = myRecyclerView.getMyGenericItemTouchListener();
        assertNotNull(touchListener);
        assertTrue(touchListener instanceof MySongItemTouchListener);
    }

    @Test
    public void testInitRecyclerViewForFolders() {
        LibraryObject parent = new LibraryObject(Category.ROOT, "id");
        MediaBrowserAdapter mediaBrowserAdapter = mock(MediaBrowserAdapter.class);
        Category category = Category.FOLDERS;
        MyGenericItemTouchListener.ItemSelectedListener listener = mock(MyGenericItemTouchListener.ItemSelectedListener.class);
        myRecyclerView.initRecyclerView(category, listener);
        RecyclerView.Adapter adapter = myRecyclerView.getAdapter();
        assertNotNull(myRecyclerView.getAdapter());
        assertTrue(adapter instanceof MyFolderViewAdapter);
        MyGenericItemTouchListener touchListener = myRecyclerView.getMyGenericItemTouchListener();
        assertNotNull(touchListener);
        assertTrue(touchListener instanceof MyFolderItemTouchListener);
    }

}