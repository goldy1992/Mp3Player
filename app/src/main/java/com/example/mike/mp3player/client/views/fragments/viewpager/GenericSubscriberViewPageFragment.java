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
    String idRequested = null;
    private static final String LOG_TAG = "GNRC_SUBSCBR_V_P_FGMT";
    public void onChildrenLoaded(LibraryId libraryId, @NonNull ArrayList<MediaBrowserCompat.MediaItem> children) {
        Log.d(LOG_TAG, "hit");
    }

    @Override
    public void itemSelected(LibraryId id) {
        MediaBrowserCompat.MediaItem item = MediaItemUtils.findMediaItemInSet(id, songs.keySet());
        if (item == null) {
            return;
        }
        List<MediaBrowserCompat.MediaItem> values = songs.get(item);
        if (values == null) {
            this.idRequested = id.getId();
            this.getMediaBrowserAdapter().subscribe(this.category, id.getId());
        } else {
            ArrayList<MediaBrowserCompat.MediaItem> valuesArrayList = new ArrayList<>(songs.get(item));
            startActivity(id.getId(), valuesArrayList);
        }
    }

    private void startActivity(String id, ArrayList<MediaBrowserCompat.MediaItem> children) {
        LibraryId libraryId = LibraryConstructor.parseId(id);
        Intent intent =  new Intent(context, activityToCall);
        intent.putExtra(PARENT_ID, libraryId.getId());
        intent.putParcelableArrayListExtra(FOLDER_CHILDREN, children);
        intent = addExtrasToIntent(libraryId, intent);
        startActivity(intent);
    }

   abstract Intent addExtrasToIntent(LibraryId is, Intent intent);
}
