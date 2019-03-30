package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.FOLDER_CHILDREN;
import static com.example.mike.mp3player.commons.Constants.FOLDER_NAME;
import static com.example.mike.mp3player.commons.Constants.PARENT_ID;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;

public abstract class GenericSubscriberViewPageFragment extends GenericViewPageFragment {
    MediaBrowserCompat.MediaItem itemRequested = null;
    private static final String LOG_TAG = "GNRC_SUBSCBR_V_P_FGMT";

    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem id) {
        String mediaId = MediaItemUtils.getMediaId(id);
        if (mediaId == null)
        {
            Log.e(LOG_TAG, "could not find MediaId for mediaItem " + id);
            return;
        }
        LibraryRequest libraryRequest = new LibraryRequest(this.category, mediaId);
        libraryRequest.putExtra(FOLDER_NAME, getTitle(id));
        startActivity(libraryRequest);
    }

    private void startActivity(String id, ArrayList<MediaBrowserCompat.MediaItem> children) {
        LibraryRequest libraryRequest = LibraryConstructor.parseId(id);
        startActivity(libraryRequest, children);
    }

    private void startActivity(LibraryRequest libraryRequest, ArrayList<MediaBrowserCompat.MediaItem> children) {

        Intent intent =  new Intent(context, activityToCall);
        intent.putExtra(PARENT_ID, libraryRequest);
        intent.putParcelableArrayListExtra(FOLDER_CHILDREN, children);
        intent = addExtrasToIntent(libraryRequest, intent);
        startActivity(intent);
    }

    private void startActivity(LibraryRequest libraryRequest) {

        Intent intent =  new Intent(context, activityToCall);
        intent.putExtra(PARENT_ID, libraryRequest);
        intent = addExtrasToIntent(libraryRequest, intent);
        startActivity(intent);
    }

    private boolean isSubscribed(MediaBrowserCompat.MediaItem item) {
        if (songs == null) {
            return false;
        }
        List<MediaBrowserCompat.MediaItem> results = songs.get(item);
        return results != null && !results.isEmpty();
    }

   abstract Intent addExtrasToIntent(LibraryRequest is, Intent intent);
}
