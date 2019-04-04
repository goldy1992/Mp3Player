package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static com.example.mike.mp3player.commons.Constants.FOLDER_CHILDREN;
import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

public abstract class GenericSubscriberViewPageFragment extends GenericViewPageFragment {
    MediaBrowserCompat.MediaItem itemRequested = null;
    private static final String LOG_TAG = "GNRC_SUBSCBR_V_P_FGMT";

    public void onChildrenLoaded(LibraryRequest libraryObject, @NonNull ArrayList<MediaBrowserCompat.MediaItem> children) {
        if (null != libraryObject && children != null) {
            MediaBrowserCompat.MediaItem mediaItem = MediaItemUtils.findMediaItemInSet(libraryObject, songs.keySet());
            songs.put(mediaItem, new ArrayList<>(children));
            LibraryConstructor.addFolderNameFromMediaItemToLibraryId(libraryObject, mediaItem);

            String idOfRequestedItem = MediaItemUtils.getMediaId(itemRequested);
            if (idOfRequestedItem != null && idOfRequestedItem.equals(libraryObject.getId())) {
                idOfRequestedItem = null;
                //IntentUtils.
                startActivity(libraryObject, children);

            }

            Log.d(LOG_TAG, "hit");

        }
    }

    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem id) {
        String mediaId = MediaItemUtils.getMediaId(id);
        if (mediaId == null)
        {
            Log.e(LOG_TAG, "could not find MediaId for mediaItem " + id);
            return;
        }
        LibraryRequest libraryObject = new LibraryRequest(this.category, mediaId);
        LibraryConstructor.addFolderNameFromMediaItemToLibraryId(libraryObject, id);
        MediaBrowserCompat.MediaItem item = MediaItemUtils.findMediaItemInSet(libraryObject, songs.keySet());
        if (item == null) {
            return;
        }

        List<MediaBrowserCompat.MediaItem> values = songs.get(item);
        if (!isSubscribed(item)) {
            this.itemRequested = item;

            this.getMediaBrowserAdapter().subscribe(libraryObject);
        } else {
            ArrayList<MediaBrowserCompat.MediaItem> valuesArrayList = new ArrayList<>(songs.get(item));
            startActivity(libraryObject, valuesArrayList);
        }
    }

    private void startActivity(String id, ArrayList<MediaBrowserCompat.MediaItem> children) {
        LibraryRequest libraryObject = LibraryConstructor.parseId(id);
        startActivity(libraryObject, children);
    }

    private void startActivity(LibraryRequest libraryObject, ArrayList<MediaBrowserCompat.MediaItem> children) {

        Intent intent =  new Intent(getContext(), activityToCall);
        intent.putExtra(REQUEST_OBJECT, libraryObject);
        intent.putParcelableArrayListExtra(FOLDER_CHILDREN, children);
        intent = addExtrasToIntent(libraryObject, intent);
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
