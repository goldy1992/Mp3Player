package com.example.mike.mp3player.client;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.views.MyRecyclerView;
import com.example.mike.mp3player.client.views.MyViewHolder;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.example.mike.mp3player.TestUtils.createMediaItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MySongViewAdapterTest {

    private MySongViewAdapter mySongViewAdapter;
    private MyRecyclerView myRecyclerView;
    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;
    private Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        LibraryObject parent = new LibraryObject(Category.SONGS, "id");
        this.mySongViewAdapter = new MySongViewAdapter();

    }

    @Test
    public void testOnCreateViewHolder() {
        ViewGroup group = mock(ViewGroup.class);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        when(group.generateLayoutParams(any(AttributeSet.class))).thenReturn(layoutParams);
        when(group.getContext()).thenReturn(this.context);
        MyViewHolder result = mySongViewAdapter.onCreateViewHolder(group, 0);
        assertNotNull(result);
        assertTrue(result.getView() instanceof GridLayout);
    }

    @Test
    public void testOnBindViewHolder() {

        // TODO: refactor to have an OnBindViewHolder setup method and test for different list indices
        List<MediaItem> mediaItems = new ArrayList<>();
        final String expectedArtist = "artist";
        final String originalDuration = "34234";
        final String expectedDuration = TimerUtils.formatTime(Long.valueOf(originalDuration));
        final String expectedTitle = "title";
        mediaItems.add(createMediaItem("101", expectedTitle, "description1", originalDuration, expectedArtist));
        mediaItems.add(createMediaItem("102", "title2", "description2", "34234"));
        mySongViewAdapter.setFilteredSongs(mediaItems);
        mySongViewAdapter.setItems(mediaItems);

        ViewGroup viewGroup = mock(ViewGroup.class);

        TextView artistView = addMockTextViewToViewGroup(viewGroup, R.id.artist);
        TextView titleView = addMockTextViewToViewGroup(viewGroup, R.id.title);
        TextView durationView = addMockTextViewToViewGroup(viewGroup, R.id.duration);


        MyViewHolder myViewHolder = mock(MyViewHolder.class);
        when(myViewHolder.getAdapterPosition()).thenReturn(0);
        when(myViewHolder.getView()).thenReturn(viewGroup);
        mySongViewAdapter.onBindViewHolder(myViewHolder, 0);

        verify(artistView, times(1)).setText(expectedArtist);
        verify(titleView, times(1)).setText(expectedTitle);
        verify(durationView, times(1)).setText(expectedDuration);
    }

    private TextView addMockTextViewToViewGroup(ViewGroup view, @IdRes int res) {
        TextView textView = mock(TextView.class);
        when(view.findViewById(res)).thenReturn(textView);
        return textView;
    }
}