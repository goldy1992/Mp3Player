package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.views.adapters.MyGenericRecycleViewAdapter;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;
import com.google.android.material.appbar.AppBarLayout;
import com.l4digital.fastscroll.FastScrollRecyclerView;

import javax.inject.Inject;

/**
 * Fragment to show a list of media items, has MediaBrowserAdapter injected into it using Dagger2
 * NOTE: MediaBrowserAdapter is not annotated with inject because:
 *
 * 1) we don't want to override the constructor for compatibility purposes
 * 2) This ChildViewFragment will be provided after the main injection is done
 */
public abstract class MediaItemListFragment extends Fragment implements MyGenericItemTouchListener.ItemSelectedListener {

    private static final String LOG_TAG = "GENRC_VIW_PGE_FRGMNT";

    /**
     * The parent for all the media items in this view; if null, the fragment represent a list of all available songs.
     */
    private FastScrollRecyclerView recyclerView;
    protected Class<? extends MediaActivityCompat> intentClass;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private MediaItemType parentItemType;
    private String parentItemTypeId;
    protected MediaBrowserAdapter mediaBrowserAdapter;
    protected MediaControllerAdapter mediaControllerAdapter;
    private MyGenericRecycleViewAdapter myViewAdapter;
    private MyGenericItemTouchListener myGenericItemTouchListener;

    public MediaItemListFragment(MediaItemType mediaItemType, String id, MediaActivityCompatComponent component) {
        this.parentItemType = mediaItemType;
        this.parentItemTypeId = id;
        injectDependencies(component);
        this.mediaBrowserAdapter.registerListener(parentItemTypeId, myViewAdapter);
        this.mediaBrowserAdapter.subscribe(parentItemTypeId);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_view_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        this.recyclerView = view.findViewById(R.id.recycler_view);
        this.recyclerView.setAdapter(myViewAdapter);
        recyclerView.setHideScrollbar(true);
        this.recyclerView.addOnItemTouchListener(myGenericItemTouchListener);
        this.myGenericItemTouchListener.setParentView(recyclerView);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setLayoutManager(linearLayoutManager);
        FixedPreloadSizeProvider<MediaBrowserCompat.MediaItem> preloadSizeProvider = new FixedPreloadSizeProvider<>(20, 20);
        RecyclerViewPreloader<MediaBrowserCompat.MediaItem> preloader =
                new RecyclerViewPreloader<>(
                        Glide.with(this), myViewAdapter, preloadSizeProvider, 10 /*maxPreload*/);

        this.recyclerView.addOnScrollListener(preloader);
    }

    @Inject
    public void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    @Inject
    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    @Inject
    public void setMyGenericRecycleViewAdapter(MyGenericRecycleViewAdapter adapter) {
        this.myViewAdapter = adapter;
    }

    @Inject
    public void setMyGenericItemTouchListener(MyGenericItemTouchListener listener) {
        this.myGenericItemTouchListener = listener;
    }

    @Inject
    public void setIntentClass(Class<? extends MediaActivityCompat> intentClass) {
        this.intentClass = intentClass;
    }

    private void injectDependencies(MediaActivityCompatComponent component) {
        component.childViewPagerFragmentSubcomponentFactory()
            .create(parentItemType, parentItemTypeId, this).
            inject(this);
    }
}
