package com.github.goldy1992.mp3player.client.views.adapters;

import androidx.annotation.Nullable;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope;

import javax.inject.Inject;

@ComponentScope
public class RecyclerViewAdapters {

    private final MySongViewAdapter mySongViewAdapter;
    private final MyFolderViewAdapter myFolderViewAdapter;

    @Inject
    public RecyclerViewAdapters(MySongViewAdapter mySongViewAdapter,
                                MyFolderViewAdapter myFolderViewAdapter) {
        this.myFolderViewAdapter = myFolderViewAdapter;
        this.mySongViewAdapter = mySongViewAdapter;
    }

    @Nullable
    public MyGenericRecycleViewAdapter getAdapter(MediaItemType mediaItemType) {
        switch (mediaItemType) {
            case SONGS:
            case FOLDER: return mySongViewAdapter;
            case FOLDERS: return myFolderViewAdapter;
            default: return null;
        }
    }
}
