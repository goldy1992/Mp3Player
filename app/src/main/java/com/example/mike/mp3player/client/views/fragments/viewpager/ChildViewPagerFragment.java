package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.views.MyRecyclerView;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import static com.example.mike.mp3player.commons.Constants.PARENT_OBJECT;
import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

/**
 * Fragment to show a list of media items, has MediaBrowserAdapter injected into it using Dagger2
 * NOTE: MediaBrowserAdapter is not annotated with inject because:
 *
 * 1) we don't want to override the constructor for compatibility purposes
 * 2) This ChildViewFragment will be provided after the main injection is done
 */
public class ChildViewPagerFragment extends Fragment implements MyGenericItemTouchListener.ItemSelectedListener {
    private static final String LOG_TAG = "GENRC_VIW_PGE_FRGMNT";

    /**
     * The parent for all the media items in this view; if null, the fragment represent a list of all available songs.
     */
    private LibraryObject parent;
    private MyRecyclerView recyclerView;
    private Category category;
    private MediaBrowserAdapter mediaBrowserAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_view_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.recyclerView = view.findViewById(R.id.myRecyclerView);
        this.recyclerView.initRecyclerView(this.category, this);
        this.mediaBrowserAdapter.registerListener(parent.getId(), this.recyclerView.getMyViewAdapter());
        this.mediaBrowserAdapter.subscribe(new LibraryRequest(parent));
    }

    public void init(Category category, LibraryObject parent) {
        this.category = category;
        this.parent = parent;
    }

    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem item) {
        String id = MediaItemUtils.getMediaId(item);
        String title = MediaItemUtils.getTitle(item);
        LibraryRequest libraryRequest = null;

        if (null != id) {
            libraryRequest = new LibraryRequest(parent.getCategory(), id);
            libraryRequest.setTitle(title);
        }

        Intent intent = new Intent(getContext(), Category.getActivityClassForCategory(category));
        intent.putExtra(PARENT_OBJECT, parent);
        intent.putExtra(REQUEST_OBJECT, libraryRequest);
        startActivity(intent);
    }

    public void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }
}
