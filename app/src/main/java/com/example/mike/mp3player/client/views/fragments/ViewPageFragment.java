package com.example.mike.mp3player.client.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MediaPlayerActvityRequester;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.activities.FolderActivity;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.Constants.FOLDER_CHILDREN;
import static com.example.mike.mp3player.commons.Constants.FOLDER_NAME;
import static com.example.mike.mp3player.commons.Constants.PARENT_ID;

public class ViewPageFragment extends GenericViewPageFragment implements MyGenericItemTouchListener.ItemSelectedListener {

    private static final String LOG_TAG = "VIEW_PAGE_FRAGMENT";

    public ViewPageFragment() {  }

    public void init(Category category, List<MediaItem> songs, MediaBrowserAdapter mediaBrowserAdapter,
                     MediaControllerAdapter mediaControllerAdapter, MediaPlayerActvityRequester mediaPlayerActvityRequester) {
        this.songs = new HashMap<>();
        for (MediaItem m : songs) {
            this.songs.put(m, null);
        }
        this.category = category;
        this.mediaBrowserAdapter = mediaBrowserAdapter;
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.mediaPlayerActvityRequester = mediaPlayerActvityRequester;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_view_page, container, false);
        view.setTag(category);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.recyclerView = view.findViewById(R.id.myRecyclerView);
        this.getRecyclerView().initRecyclerView(category, new ArrayList<>(songs.keySet()), mediaBrowserAdapter,
                mediaControllerAdapter, mediaPlayerActvityRequester);
    }

    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaItem> children,
                                 @NonNull Bundle options, Context context) {
    }




    public static ViewPageFragment createAndInitialiseViewPageFragment(Category category, List<MediaItem> songs, MediaBrowserAdapter mediaBrowserAdapter,
                                                                       MediaControllerAdapter mediaControllerAdapter, MediaPlayerActvityRequester mediaPlayerActvityRequester) {
        ViewPageFragment viewPageFragment = new ViewPageFragment();
        viewPageFragment.init(category, songs, mediaBrowserAdapter, mediaControllerAdapter);
        return viewPageFragment;
    }

    @Override
    public void itemSelected(String id) {
        MediaItem item = MediaItemUtils.findMediaItemInSet(id, songs.keySet());
        if (item == null) {
            return;
        }
        ArrayList<MediaItem> values = songs.get(item);
        if (values == null) {
            this.idRequested = id;
            this.mediaBrowserAdapter.subscribe(this.category, id);
        } else {
            startActivity(id, values);
        }
    }

    private void startActivity(String id, ArrayList<MediaItem> children) {
        LibraryId libraryId = LibraryConstructor.parseId(id);
        if (libraryId.getCategory().equals(Category.SONGS)) {
            mediaPlayerActvityRequester.playSelectedSong(libraryId.getId());
        } else {
            Class<?> activityToCall = null;
            switch (libraryId.getCategory()) {
                case FOLDERS:
                    activityToCall = FolderActivity.class;
                    break;
                default:
                    activityToCall = null;
            }
            startActivity(activityToCall, libraryId, children);
        }
    }

    private void startActivity(Class<?> activity, LibraryId libraryId, ArrayList<MediaItem> children) {
        Intent intent =  new Intent(context, activity);
        intent.putExtra(PARENT_ID, libraryId.getId());
        String folderName = libraryId.getExtra(FOLDER_NAME);
        if (folderName  != null) {
            intent.putExtra(FOLDER_NAME, folderName);
        }
        intent.putParcelableArrayListExtra(FOLDER_CHILDREN, children);
        startActivity(intent);
    }
}

