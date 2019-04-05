package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.views.MyRecyclerView;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;
import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

public class GenericViewPageFragment extends Fragment implements MyGenericItemTouchListener.ItemSelectedListener {
    private static final String LOG_TAG = "GENRC_VIW_PGE_FRGMNT";
    /**
     * The parent for all the media items in this view; if null, the fragment represent a list of all available songs.
     */
    LibraryObject parent;
    MyRecyclerView recyclerView;
    Class<?> activityToCall;

    /* TODO: add mechanism to store children in the fragment without having to repoll for the same data */
    Map<MediaItem, List<MediaItem>> songs;
    private MediaBrowserAdapter mediaBrowserAdapter;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_view_page, container, false);
        //view.setTag(parent.getCategory());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.recyclerView = view.findViewById(R.id.myRecyclerView);
        this.recyclerView.initRecyclerView(parent, mediaBrowserAdapter, this);
    }

    public void init(Class<?> activityToCall, LibraryObject parent, MediaBrowserAdapter mediaBrowserAdapter) {
        this.activityToCall = activityToCall;
        this.parent = parent;
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem item) {
        String id = MediaItemUtils.getMediaId(item);
        String title = MediaItemUtils.getTitle(item);
        LibraryRequest libraryRequest = new LibraryRequest(parent.getCategory(), id);
        libraryRequest.setTitle(title);
        Intent intent = new Intent(getContext(), activityToCall);
        intent.putExtra(REQUEST_OBJECT, libraryRequest);
        intent.putExtra(MEDIA_SESSION, mediaBrowserAdapter.getMediaSessionToken());
        startActivity(intent);
    }

    public MyRecyclerView getRecyclerView() {
        return recyclerView;
    }

    public MediaBrowserAdapter getMediaBrowserAdapter() {
        return mediaBrowserAdapter;
    }

    /**
     *
     * @param category the category of the view, this will help to know which activity should be called.
     * @param parent to know which MediaItem needs to be subscribed to.
     * @param mediaBrowserAdapter used to register the appropriate listeners
     * @return
     */
    public static GenericViewPageFragment createViewPageFragment(Category category, LibraryObject parent, MediaBrowserAdapter mediaBrowserAdapter) {
        GenericViewPageFragment viewPageFragment = new GenericViewPageFragment();
        viewPageFragment.init(activityToCall, libraryObject, mediaBrowserAdapter);
        return viewPageFragment;
    }
}
