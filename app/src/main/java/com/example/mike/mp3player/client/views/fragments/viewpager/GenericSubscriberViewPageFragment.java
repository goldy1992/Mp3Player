package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static com.example.mike.mp3player.commons.Constants.FOLDER_CHILDREN;
import static com.example.mike.mp3player.commons.Constants.PARENT_ID;

public abstract class GenericSubscriberViewPageFragment extends GenericViewPageFragment {
    MediaBrowserCompat.MediaItem itemRequested = null;
    private static final String LOG_TAG = "GNRC_SUBSCBR_V_P_FGMT";

    public void onChildrenLoaded(LibraryId libraryId, @NonNull ArrayList<MediaBrowserCompat.MediaItem> children) {
        if (null != libraryId && children != null) {
            MediaBrowserCompat.MediaItem mediaItem = MediaItemUtils.findMediaItemInSet(libraryId, songs.keySet());
            songs.put(mediaItem, new ArrayList<>(children));
            LibraryConstructor.addFolderNameFromMediaItemToLibraryId(libraryId, mediaItem);

            String idOfRequestedItem = MediaItemUtils.getMediaId(itemRequested);
            if (idOfRequestedItem != null && idOfRequestedItem.equals(libraryId.getId())) {
                idOfRequestedItem = null;
                //IntentUtils.
                startActivity(libraryId, children);

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
        LibraryId libraryId = new LibraryId(this.category, mediaId);
        MediaBrowserCompat.MediaItem item = MediaItemUtils.findMediaItemInSet(libraryId, songs.keySet());
        if (item == null) {
            return;
        }

        List<MediaBrowserCompat.MediaItem> values = songs.get(item);
        if (!isSubscribed(item)) {
            this.itemRequested = item;

            this.getMediaBrowserAdapter().subscribe(libraryId);
        } else {
            ArrayList<MediaBrowserCompat.MediaItem> valuesArrayList = new ArrayList<>(songs.get(item));
            startActivity(libraryId, valuesArrayList);
        }
    }

    private void startActivity(String id, ArrayList<MediaBrowserCompat.MediaItem> children) {
        LibraryId libraryId = LibraryConstructor.parseId(id);
        startActivity(libraryId, children);
    }

    private void startActivity(LibraryId libraryId, ArrayList<MediaBrowserCompat.MediaItem> children) {

        Intent intent =  new Intent(context, activityToCall);
        intent.putExtra(PARENT_ID, libraryId);
        intent.putParcelableArrayListExtra(FOLDER_CHILDREN, children);
        intent = addExtrasToIntent(libraryId, intent);
        startActivity(intent);
    }

    private boolean isSubscribed(MediaBrowserCompat.MediaItem item) {
        if (songs == null) {
            return false;
        }
        List<MediaBrowserCompat.MediaItem> results = songs.get(item);
        return results != null && !results.isEmpty();
    }

   abstract Intent addExtrasToIntent(LibraryId is, Intent intent);
}
