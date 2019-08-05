package com.example.mike.mp3player.client;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.views.MySongViewAdapter;
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
import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
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
    @Mock
    private AlbumArtPainter albumArtPainter;

    private TextView titleView;
    private TextView artistView;
    private TextView durationView;
    private ViewGroup viewGroup;
    private List<MediaItem> mediaItems;
    private Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        LibraryObject parent = new LibraryObject(Category.SONGS, "id");
        this.mySongViewAdapter = new MySongViewAdapter(albumArtPainter);
        this.mediaItems = new ArrayList<>();
        this.viewGroup = mock(ViewGroup.class);
        this.artistView = addMockTextViewToViewGroup(viewGroup, R.id.artist);
        this.titleView = addMockTextViewToViewGroup(viewGroup, R.id.title);
        this.durationView = addMockTextViewToViewGroup(viewGroup, R.id.duration);
    }

    @Test
    public void testOnCreateViewHolder() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        when(viewGroup.generateLayoutParams(any(AttributeSet.class))).thenReturn(layoutParams);
        when(viewGroup.getContext()).thenReturn(this.context);
        MyViewHolder result = mySongViewAdapter.onCreateViewHolder(viewGroup, 0);
        assertNotNull(result);
        assertTrue(result.getView() instanceof LinearLayout);
    }

    @Test
    public void testOnBindViewHolderEmptyValues() {
        final String expectedArtist = UNKNOWN;
        final String expectedTitle = "";
        this.mediaItems.add(createMediaItem("101", null, "description1", "45646", expectedArtist));

        bindViewHolder();
        verify(artistView, times(1)).setText(expectedArtist);
        verify(titleView, times(1)).setText(expectedTitle);
    }

    @Test
    public void testBindNullTitleUseFileName() {
        final String fileName = "FILE_NAME";
        final String extension = ".mp3";
        final String fullFileName = fileName + extension;
        MediaItem mediaItem = createMediaItem("ID", null, "description", "32525");
        mediaItem.getDescription().getExtras().putString(META_DATA_KEY_FILE_NAME, fullFileName);
        this.mediaItems.add(mediaItem);
        bindViewHolder();
        verify(titleView, times(1)).setText(fileName);

    }

    @Test
    public void testOnBindViewHolder() {

        // TODO: refactor to have an OnBindViewHolder setup method and test for different list indices
        final String expectedArtist = "artist";
        final String originalDuration = "34234";
        final String expectedDuration = TimerUtils.formatTime(Long.valueOf(originalDuration));
        final String expectedTitle = "title";
        mediaItems.add(createMediaItem("101", expectedTitle, "description1", originalDuration, expectedArtist));
        mediaItems.add(createMediaItem("102", "title2", "description2", "34234"));

        bindViewHolder();
        verify(artistView, times(1)).setText(expectedArtist);
        verify(titleView, times(1)).setText(expectedTitle);
        verify(durationView, times(1)).setText(expectedDuration);
    }

    private void bindViewHolder() {
        mySongViewAdapter.setItems(mediaItems);

        MyViewHolder myViewHolder = mock(MyViewHolder.class);
        when(myViewHolder.getAdapterPosition()).thenReturn(0);
        when(myViewHolder.getView()).thenReturn(viewGroup);
        mySongViewAdapter.onBindViewHolder(myViewHolder, 0);
    }
    private TextView addMockTextViewToViewGroup(ViewGroup view, @IdRes int res) {
        TextView textView = mock(TextView.class);
        when(view.findViewById(res)).thenReturn(textView);
        return textView;
    }
}