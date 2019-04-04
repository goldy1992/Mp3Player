package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.FolderActivity;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.List;

import static com.example.mike.mp3player.commons.Constants.FOLDER_NAME;

public class FolderViewPageFragment extends GenericSubscriberViewPageFragment {

    @Override
    Intent addExtrasToIntent(LibraryRequest libraryObject, Intent intent) {
        String folderName = libraryObject.getExtra(FOLDER_NAME);
        if (folderName  != null) {
            intent.putExtra(FOLDER_NAME, folderName);
        }
        return intent;
    }

    @Override
    Class<?> getActivityToCall() {
        return FolderActivity.class;
    }
}
